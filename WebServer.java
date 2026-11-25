import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Web front-end bridge for the Banking Transaction System.
 *
 * This class does NOT modify any existing code. It simply exposes the
 * existing AccountDAO methods as a small JSON API and serves the
 * HTML/CSS/JS files from the "web" folder.
 *
 * Run:  java -cp "bin;lib/*" WebServer      (Windows)
 *       java -cp "bin:lib/*" WebServer      (Mac/Linux)
 * Open: http://localhost:8080
 */
public class WebServer {

    private static final int PORT = 8080;

    private static final AccountDAO dao = new AccountDAO();

    // token -> session
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final SecureRandom random = new SecureRandom();

    private static class Session {
        final String role;      // CUSTOMER or ADMIN
        final int account;      // -1 for admin
        final String username;

        Session(String role, int account, String username) {
            this.role = role;
            this.account = account;
            this.username = username;
        }
    }

    private static Path webRoot;

    public static void main(String[] args) throws IOException {

        webRoot = findWebRoot();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api/", WebServer::handleApi);
        server.createContext("/", WebServer::handleStatic);
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();

        System.out.println("=====================================");
        System.out.println(" ABC Bank web UI running");
        System.out.println(" Open:  http://localhost:" + PORT);
        System.out.println(" Files: " + webRoot.toAbsolutePath());
        System.out.println("=====================================");
    }

    // ------------------------------------------------------------------
    // Static files
    // ------------------------------------------------------------------

    private static Path findWebRoot() {
        String[] candidates = {"web", "../web", "Bank_Transaction_System/web"};
        for (String c : candidates) {
            Path p = Paths.get(c).toAbsolutePath().normalize();
            if (Files.isDirectory(p)) {
                return p;
            }
        }
        throw new IllegalStateException(
            "Could not find the 'web' folder. Run from the project root.");
    }

    private static void handleStatic(HttpExchange ex) throws IOException {

        String path = ex.getRequestURI().getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }

        Path file = webRoot.resolve(path.substring(1)).normalize();

        if (!file.startsWith(webRoot) || !Files.isRegularFile(file)) {
            send(ex, 404, "text/plain; charset=utf-8", "Not found");
            return;
        }

        byte[] data = Files.readAllBytes(file);
        ex.getResponseHeaders().set("Content-Type", mime(file.toString()));
        ex.getResponseHeaders().set("Cache-Control", "no-cache");
        ex.sendResponseHeaders(200, data.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(data);
        }
    }

    private static String mime(String name) {
        if (name.endsWith(".html")) return "text/html; charset=utf-8";
        if (name.endsWith(".css")) return "text/css; charset=utf-8";
        if (name.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (name.endsWith(".svg")) return "image/svg+xml";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }

    // ------------------------------------------------------------------
    // API
    // ------------------------------------------------------------------

    private static void handleApi(HttpExchange ex) throws IOException {

        try {
            String path = ex.getRequestURI().getPath();
            String method = ex.getRequestMethod();
            String body = readBody(ex);

            // ---- public ----
            if (path.equals("/api/login") && method.equals("POST")) {
                login(ex, body);
                return;
            }

            // ---- authenticated ----
            Session s = auth(ex);
            if (s == null) {
                json(ex, 401, err("Session expired. Please sign in again."));
                return;
            }

            if (path.equals("/api/logout") && method.equals("POST")) {
                sessions.remove(token(ex));
                json(ex, 200, "{\"ok\":true}");
                return;
            }

            if (path.startsWith("/api/admin/")) {
                if (!s.role.equals("ADMIN")) {
                    json(ex, 403, err("Admin access required."));
                    return;
                }
                admin(ex, path, method, body);
                return;
            }

            if (!s.role.equals("CUSTOMER")) {
                json(ex, 403, err("Customer access required."));
                return;
            }

            customer(ex, s, path, method, body);

        } catch (Exception e) {
            e.printStackTrace();
            json(ex, 500, err("Server error: " + e.getMessage()));
        }
    }

    private static void login(HttpExchange ex, String body) throws IOException {

        String role = str(body, "role");
        String username = str(body, "username");
        String password = str(body, "password");

        if (username.isEmpty() || password.isEmpty()) {
            json(ex, 400, err("Please enter username and password."));
            return;
        }

        if ("ADMIN".equals(role)) {
            if (dao.authenticateAdmin(username, password)) {
                String t = newToken();
                sessions.put(t, new Session("ADMIN", -1, username));
                json(ex, 200, "{\"token\":\"" + t + "\",\"role\":\"ADMIN\"}");
            } else {
                json(ex, 401, err("Invalid admin username or password."));
            }
            return;
        }

        int acc = dao.authenticateCustomer(username, password);
        if (acc != -1) {
            String t = newToken();
            sessions.put(t, new Session("CUSTOMER", acc, username));
            json(ex, 200, "{\"token\":\"" + t + "\",\"role\":\"CUSTOMER\"}");
        } else {
            json(ex, 401, err("Invalid username or password."));
        }
    }

    private static void customer(HttpExchange ex, Session s, String path,
                                 String method, String body) throws IOException {

        int acc = s.account;

        switch (path) {

            case "/api/me": {
                double bal = dao.getBalance(acc);
                String name = s.username;
                for (String[] c : parseCustomers()) {
                    if (c[0].equals(String.valueOf(acc))) {
                        name = c[1];
                    }
                }
                json(ex, 200, "{\"role\":\"CUSTOMER\",\"username\":" + q(s.username)
                    + ",\"name\":" + q(name)
                    + ",\"account\":" + acc
                    + ",\"balance\":" + num(bal) + "}");
                return;
            }

            case "/api/history": {
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (String line : dao.getTransactionHistory(acc)) {
                    // Type | Amount: Rs. X | Date: Y
                    String[] p = line.split(" \\| ");
                    if (p.length < 3) continue;
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{\"type\":").append(q(p[0].trim()))
                      .append(",\"amount\":").append(num(parseRs(p[1])))
                      .append(",\"date\":").append(q(p[2].replace("Date:", "").trim()))
                      .append("}");
                }
                json(ex, 200, sb.append("]").toString());
                return;
            }

            case "/api/deposit": {
                double amount = dbl(body, "amount");
                boolean ok = dao.deposit(acc, amount);
                result(ex, ok, "Deposit successful.", "Deposit failed. Enter a valid amount.", acc);
                return;
            }

            case "/api/withdraw": {
                double amount = dbl(body, "amount");
                if (amount > 0 && amount > dao.getBalance(acc)) {
                    json(ex, 400, err("Insufficient balance."));
                    return;
                }
                boolean ok = dao.withdraw(acc, amount);
                result(ex, ok, "Withdrawal successful.", "Withdrawal failed. Check the amount.", acc);
                return;
            }

            case "/api/transfer": {
                int to = (int) dbl(body, "to");
                double amount = dbl(body, "amount");

                if (to == acc) {
                    json(ex, 400, err("You cannot transfer money to your own account."));
                    return;
                }
                if (to <= 0 || dao.getBalance(to) < 0) {
                    json(ex, 400, err("Receiver account not found."));
                    return;
                }
                if (amount <= 0 || Double.isNaN(amount)) {
                    json(ex, 400, err("Enter a valid amount."));
                    return;
                }
                if (amount > dao.getBalance(acc)) {
                    json(ex, 400, err("Insufficient balance."));
                    return;
                }
                boolean ok = dao.transfer(acc, to, amount);
                result(ex, ok, "Transfer successful.", "Transfer failed. Please try again.", acc);
                return;
            }

            default:
                json(ex, 404, err("Unknown endpoint."));
        }
    }

    private static void result(HttpExchange ex, boolean ok, String good,
                               String bad, int acc) throws IOException {
        if (ok) {
            json(ex, 200, "{\"ok\":true,\"message\":" + q(good)
                + ",\"balance\":" + num(dao.getBalance(acc)) + "}");
        } else {
            json(ex, 400, err(bad));
        }
    }

    private static void admin(HttpExchange ex, String path, String method,
                              String body) throws IOException {

        switch (path) {

            case "/api/admin/customers": {
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (String[] c : parseCustomers()) {
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{\"account\":").append(c[0])
                      .append(",\"name\":").append(q(c[1]))
                      .append(",\"balance\":").append(c[2]).append("}");
                }
                json(ex, 200, sb.append("]").toString());
                return;
            }

            case "/api/admin/transactions": {
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (String block : dao.getAllTransactions()) {
                    String id = field(block, "Transaction ID:");
                    String acct = field(block, "Account Number:");
                    String recv = field(block, "Receiver Account:");
                    String type = field(block, "Type:");
                    String amt = field(block, "Amount: Rs.");
                    String date = field(block, "Date:");

                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{\"id\":").append(safeNum(id))
                      .append(",\"account\":").append(safeNum(acct))
                      .append(",\"receiver\":")
                      .append(recv.equals("null") || recv.isEmpty() ? "null" : safeNum(recv))
                      .append(",\"type\":").append(q(type))
                      .append(",\"amount\":").append(safeNum(amt))
                      .append(",\"date\":").append(q(date)).append("}");
                }
                json(ex, 200, sb.append("]").toString());
                return;
            }

            case "/api/admin/total": {
                json(ex, 200, "{\"total\":" + num(dao.getTotalBankBalance()) + "}");
                return;
            }

            case "/api/admin/add": {
                int acc = (int) dbl(body, "account");
                String name = str(body, "name");
                double bal = dbl(body, "balance");
                if (dao.addAccount(acc, name, bal)) {
                    json(ex, 200, "{\"ok\":true,\"message\":\"Customer account created.\"}");
                } else {
                    json(ex, 400, err("Account creation failed. Check the details or account number."));
                }
                return;
            }

            case "/api/admin/delete": {
                int acc = (int) dbl(body, "account");
                if (dao.deleteAccount(acc)) {
                    json(ex, 200, "{\"ok\":true,\"message\":\"Account deleted.\"}");
                } else {
                    json(ex, 400, err("Could not delete account. It may not exist or may have linked records."));
                }
                return;
            }

            default:
                json(ex, 404, err("Unknown endpoint."));
        }
    }

    // ------------------------------------------------------------------
    // Parsing helpers (DAO returns formatted strings)
    // ------------------------------------------------------------------

    private static final Pattern CUSTOMER = Pattern.compile(
        "Account Number: (\\d+) \\| Customer Name: (.*) \\| Balance: Rs\\. (.*)");

    private static List<String[]> parseCustomers() {
        List<String[]> out = new ArrayList<>();
        for (String line : dao.getAllCustomers()) {
            Matcher m = CUSTOMER.matcher(line);
            if (m.matches()) {
                out.add(new String[]{m.group(1), m.group(2), m.group(3).trim()});
            }
        }
        return out;
    }

    private static double parseRs(String s) {
        try {
            return Double.parseDouble(s.replace("Amount: Rs.", "").trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static String field(String block, String label) {
        for (String line : block.split("\n")) {
            if (line.startsWith(label)) {
                return line.substring(label.length()).trim();
            }
        }
        return "";
    }

    private static String safeNum(String s) {
        try {
            double d = Double.parseDouble(s.trim());
            return num(d);
        } catch (Exception e) {
            return "0";
        }
    }

    // ------------------------------------------------------------------
    // Tiny JSON helpers (no external libraries needed)
    // ------------------------------------------------------------------

    private static String str(String json, String key) {
        Matcher m = Pattern.compile(
            "\"" + key + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(json);
        if (m.find()) {
            return m.group(1).replace("\\\"", "\"").replace("\\\\", "\\").trim();
        }
        // number given as plain value
        Matcher n = Pattern.compile(
            "\"" + key + "\"\\s*:\\s*(-?[0-9.]+)").matcher(json);
        return n.find() ? n.group(1) : "";
    }

    private static double dbl(String json, String key) {
        try {
            return Double.parseDouble(str(json, key));
        } catch (Exception e) {
            return -1;
        }
    }

    private static String q(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.append("\"").toString();
    }

    private static String num(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) return "0";
        return java.math.BigDecimal.valueOf(d).toPlainString();
    }

    private static String err(String message) {
        return "{\"error\":" + q(message) + "}";
    }

    // ------------------------------------------------------------------
    // HTTP helpers
    // ------------------------------------------------------------------

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static String token(HttpExchange ex) {
        String h = ex.getRequestHeaders().getFirst("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            return h.substring(7).trim();
        }
        return "";
    }

    private static Session auth(HttpExchange ex) {
        return sessions.get(token(ex));
    }

    private static String newToken() {
        byte[] b = new byte[24];
        random.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    private static void json(HttpExchange ex, int code, String body) throws IOException {
        send(ex, code, "application/json; charset=utf-8", body);
    }

    private static void send(HttpExchange ex, int code, String type, String body)
            throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", type);
        ex.getResponseHeaders().set("Cache-Control", "no-store");
        ex.sendResponseHeaders(code, data.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(data);
        }
    }
}
