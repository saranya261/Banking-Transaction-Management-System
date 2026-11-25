
import java.util.ArrayList;

public class BankService {

    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;

    public BankService() {

        accounts = new ArrayList<>();
        transactions = new ArrayList<>();

        // Creating two sample customer accounts
        accounts.add(new Account(101, "Sneha", 5000));
        accounts.add(new Account(102, "Rahul", 3000));
    }

    // Find account using account number
    private Account findAccount(int accountNumber) {

        for (Account account : accounts) {

            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }

        return null;
    }

    // Balance Enquiry
    public synchronized double checkBalance() {
        return accounts.get(0).getBalance();
    }

    // Deposit
    public synchronized void deposit(double amount) {

        Account sender = accounts.get(0);

        if (amount <= 0 || !Double.isFinite(amount)) {
            return;
        }

        sender.deposit(amount);

        transactions.add(
            new Transaction("Deposit", amount, sender.getBalance())
        );
    }

    // Withdrawal
    public synchronized void withdraw(double amount) {

        Account sender = accounts.get(0);

        if (amount > 0 && amount <= sender.getBalance()) {

            sender.withdraw(amount);

            transactions.add(
                new Transaction("Withdrawal", amount, sender.getBalance())
            );
        }
    }

    // Fund Transfer
    public synchronized String transfer(int receiverAccountNumber, double amount) {

        Account sender = accounts.get(0);
        Account receiver = findAccount(receiverAccountNumber);

        if (receiver == null) {
            return "Receiver account not found!";
        }

        if (sender.getAccountNumber() == receiverAccountNumber) {
            return "You cannot transfer money to your own account!";
        }

        if (amount <= 0 || !Double.isFinite(amount)) {
            return "Enter a valid amount!";
        }

        if (amount > sender.getBalance()) {
            return "Insufficient balance!";
        }

        sender.withdraw(amount);
        receiver.deposit(amount);

        transactions.add(
            new Transaction(
                "Transfer to " + receiverAccountNumber,
                amount,
                sender.getBalance()
            )
        );

        return "Transfer successful!";
    }

    // Transaction History
    public synchronized ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}
