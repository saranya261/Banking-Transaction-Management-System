# 🏦 SECURE BANK
### Banking Transaction Management System

**Smarter Banking. Simpler Transactions. Secure Experience.**

Secure Bank is a Java-based Banking Transaction Management System that brings essential banking operations into one integrated application. It combines Java backend processing, MySQL database management, and an interactive web interface to provide customers and administrators with a centralized banking experience.

The project supports both a Java Swing desktop application and a browser-based interface.

---

## ✨ About the Project

Secure Bank aims to simplify everyday banking operations through a system that connects customer account management, financial transactions, and administrative activities.

With separate customer and administrator modules, the application provides an organized way to manage accounts, process transactions, and maintain banking records.

---

## 🚀 Key Features

### 👤 Customer Banking
- Customer authentication and login
- Check account balance
- Deposit money
- Withdraw money
- Transfer funds between accounts
- View transaction history

### 🛡️ Admin Management
- Administrator authentication
- View customer accounts
- Monitor banking transactions
- Add customer accounts
- Delete customer accounts
- View total bank balance

### 💳 Transaction Management
- Deposit and withdrawal processing
- Fund transfer operations
- Balance validation
- Transaction history maintenance
- Database transaction handling with commit and rollback

### 💻 Multiple Interfaces
- Interactive web-based banking interface
- Java Swing desktop application
- Java HTTP server for web application communication

---

## 🧰 Technology Stack

| Technology | Purpose |
|---|---|
| Java | Backend logic |
| Java Swing | Desktop interface |
| JDBC | Database connectivity |
| MySQL | Database management |
| Java HTTP Server | Web server and API |
| HTML5 | Frontend structure |
| CSS3 | Frontend styling |
| JavaScript | Frontend interactions |

---

## 🏗️ System Architecture

```text
                 SECURE BANK
                      |
            +---------+---------+
            |                   |
       Web Interface        Swing GUI
            |                   |
       HTML/CSS/JS           Java Swing
            |                   |
            +---------+---------+
                      |
                 Java Backend
                      |
                 JDBC / DAO
                      |
                    MySQL
```

---

## 📂 Project Structure

```text
Secure_Bank/
│
├── src/
│   ├── Java Source Files
│   ├── Account Management
│   ├── Transaction Management
│   ├── Database Connectivity
│   └── Dashboard Modules
│
├── web/
│   ├── index.html
│   ├── style.css
│   ├── app.js
│   └── fx.js
│
├── lib/
│
├── run.bat
├── run.sh
├── WEB_UI_README.md
├── .gitignore
└── README.md
```

---

## ⚡ Getting Started

### Prerequisites
- Java JDK
- MySQL Server
- MySQL Connector/J

### 1. Clone the Repository

```bash
git clone YOUR_REPOSITORY_URL
```

### 2. Navigate to the Project

```bash
cd Secure_Bank
```

### 3. Configure the Database

- Start MySQL Server.
- Create the required banking database.
- Set up the required tables.
- Configure database credentials using environment variables.

### 4. Configure Dependencies

Place the MySQL JDBC Connector inside the `lib` directory.

### 5. Run the Application

**Windows:**

```bash
run.bat
```

**Linux / macOS:**

```bash
chmod +x run.sh
./run.sh
```

### 6. Access the Web Interface

```text
http://localhost:8080
```

---

## 🧠 Concepts Demonstrated

- Object-Oriented Programming
- Java Classes and Objects
- Encapsulation
- Exception Handling
- JDBC Connectivity
- SQL Database Operations
- Prepared Statements
- Database Transactions
- Frontend and Backend Integration
- HTTP Communication

---

## 👥 Team Members

| Role | Contributor |
|---|---|
| Project Developer | Your Name |
| Team Member 2 | Friend 1 |
| Team Member 3 | Friend 2 |

---

## 🔮 Future Enhancements

- Enhanced authentication and password security
- Detailed financial analytics
- Account profile management
- Transaction notifications
- Advanced transaction reports
- Cloud deployment

---

## ⚠️ Disclaimer

Secure Bank is an academic project developed for educational and demonstration purposes. It is not intended for actual banking operations or real financial transactions.

---

**SECURE BANK — Smarter Banking. Simpler Transactions.**
