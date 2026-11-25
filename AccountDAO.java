
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AccountDAO {

    // Balance Enquiry
    public double getBalance(int accountNumber) {

        String query = "SELECT balance FROM Accounts WHERE account_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Deposit Money
    public boolean deposit(int accountNumber, double amount) {

        if (amount <= 0 || !Double.isFinite(amount)) {
            return false;
        }

        String updateQuery =
            "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

        String insertQuery =
            "INSERT INTO Transactions (account_number, transaction_type, amount) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(updateQuery);
                 PreparedStatement ts = con.prepareStatement(insertQuery)) {

                ps.setDouble(1, amount);
                ps.setInt(2, accountNumber);

                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }

                ts.setInt(1, accountNumber);
                ts.setString(2, "Deposit");
                ts.setDouble(3, amount);
                ts.executeUpdate();

                con.commit();
                return true;

            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Withdraw Money
    public boolean withdraw(int accountNumber, double amount) {

        if (amount <= 0 || !Double.isFinite(amount)) {
            return false;
        }

        String updateQuery =
            "UPDATE Accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?";

        String insertQuery =
            "INSERT INTO Transactions (account_number, transaction_type, amount) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(updateQuery);
                 PreparedStatement ts = con.prepareStatement(insertQuery)) {

                ps.setDouble(1, amount);
                ps.setInt(2, accountNumber);
                ps.setDouble(3, amount);

                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }

                ts.setInt(1, accountNumber);
                ts.setString(2, "Withdrawal");
                ts.setDouble(3, amount);
                ts.executeUpdate();

                con.commit();
                return true;

            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Transaction History
    public ArrayList<String> getTransactionHistory(int accountNumber) {

        ArrayList<String> history = new ArrayList<>();

        String query =
            "SELECT transaction_type, amount, transaction_date " +
            "FROM Transactions WHERE account_number = ? " +
            "ORDER BY transaction_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, accountNumber);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String details =
                    rs.getString("transaction_type")
                    + " | Amount: Rs. "
                    + rs.getDouble("amount")
                    + " | Date: "
                    + rs.getTimestamp("transaction_date");

                history.add(details);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return history;
    }

    // Fund Transfer
    public boolean transfer(int senderAccount, int receiverAccount,
                            double amount) {

        if (senderAccount == receiverAccount ||
            amount <= 0 || !Double.isFinite(amount)) {
            return false;
        }

        String withdrawQuery =
            "UPDATE Accounts SET balance = balance - ? " +
            "WHERE account_number = ? AND balance >= ?";

        String depositQuery =
            "UPDATE Accounts SET balance = balance + ? " +
            "WHERE account_number = ?";

        String transactionQuery =
            "INSERT INTO Transactions " +
            "(account_number, receiver_account_number, transaction_type, amount) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (
                PreparedStatement withdrawPS =
                    con.prepareStatement(withdrawQuery);

                PreparedStatement depositPS =
                    con.prepareStatement(depositQuery);

                PreparedStatement transactionPS =
                    con.prepareStatement(transactionQuery)
            ) {

                withdrawPS.setDouble(1, amount);
                withdrawPS.setInt(2, senderAccount);
                withdrawPS.setDouble(3, amount);

                if (withdrawPS.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }

                depositPS.setDouble(1, amount);
                depositPS.setInt(2, receiverAccount);

                if (depositPS.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }

                transactionPS.setInt(1, senderAccount);
                transactionPS.setInt(2, receiverAccount);
                transactionPS.setString(3, "Transfer");
                transactionPS.setDouble(4, amount);

                transactionPS.executeUpdate();

                con.commit();
                return true;

            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // View All Customers (Admin)
    public ArrayList<String> getAllCustomers() {

        ArrayList<String> customers = new ArrayList<>();

        String query = "SELECT * FROM Accounts";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String details =
                    "Account Number: " + rs.getInt("account_number")
                    + " | Customer Name: " + rs.getString("customer_name")
                    + " | Balance: Rs. " + rs.getDouble("balance");

                customers.add(details);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

    // View All Transactions (Admin)
    public ArrayList<String> getAllTransactions() {

        ArrayList<String> transactions = new ArrayList<>();

        String query =
            "SELECT * FROM Transactions ORDER BY transaction_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String details =
                    "Transaction ID: " + rs.getInt("transaction_id")
                    + "\nAccount Number: " + rs.getInt("account_number")
                    + "\nReceiver Account: " + rs.getString("receiver_account_number")
                    + "\nType: " + rs.getString("transaction_type")
                    + "\nAmount: Rs. " + rs.getDouble("amount")
                    + "\nDate: " + rs.getTimestamp("transaction_date");

                transactions.add(details);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    // Total Bank Balance (Admin)
    public double getTotalBankBalance() {

        String query =
            "SELECT SUM(balance) AS total_balance FROM Accounts";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total_balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Add New Customer Account (Admin)
    public boolean addAccount(int accountNumber, String customerName,
                              double initialBalance) {

        if (accountNumber <= 0 ||
            customerName == null ||
            customerName.trim().isEmpty() ||
            initialBalance < 0 ||
            !Double.isFinite(initialBalance)) {

            return false;
        }

        String query =
            "INSERT INTO Accounts (account_number, customer_name, balance) " +
            "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, accountNumber);
            ps.setString(2, customerName.trim());
            ps.setDouble(3, initialBalance);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Customer Account (Admin)
    public boolean deleteAccount(int accountNumber) {

        if (accountNumber <= 0) {
            return false;
        }

        String query =
            "DELETE FROM Accounts WHERE account_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, accountNumber);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Customer Login Authentication
    public int authenticateCustomer(String username, String password) {

        String query =
            "SELECT account_number FROM Users " +
            "WHERE username = ? AND password = ? AND role = 'CUSTOMER'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int accountNumber = rs.getInt("account_number");

                if (!rs.wasNull()) {
                    return accountNumber;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Admin Login Authentication
    public boolean authenticateAdmin(String username, String password) {

        String query =
            "SELECT * FROM Users " +
            "WHERE username = ? AND password = ? AND role = 'ADMIN'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
