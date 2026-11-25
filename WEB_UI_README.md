# Secure Bank – Web UI

Your Java code is untouched. Added:
- src/WebServer.java  – built-in HTTP server that calls your existing AccountDAO
- web/                – the UI (index.html, style.css, app.js)
- run.bat / run.sh    – compile + start

## Run
1. Copy your MySQL connector jar into a folder named `lib` in the project root.
2. Make sure MySQL is running with your BankingSystem database.
3. Run run.bat (Windows) or ./run.sh (Mac/Linux) from the project root.
4. Open http://localhost:8080

Your old Swing app (Main.java) still works as before.
