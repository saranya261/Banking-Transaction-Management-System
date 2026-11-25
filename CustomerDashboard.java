
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class CustomerDashboard extends JFrame {

    private AccountDAO accountDAO;
    private int accountNumber;

    // Constructor receives authenticated account number
    public CustomerDashboard(int accountNumber) {

        this.accountNumber = accountNumber;
        accountDAO = new AccountDAO();

        setTitle("Customer Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 15));
        panel.setBackground(new Color(230, 240, 250));

        JLabel title = new JLabel(
            "CUSTOMER DASHBOARD",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton balanceButton = new JButton("Balance Enquiry");
        JButton depositButton = new JButton("Deposit Money");
        JButton withdrawButton = new JButton("Withdraw Money");
        JButton transferButton = new JButton("Fund Transfer");
        JButton historyButton = new JButton("Transaction History");

        panel.add(title);
        panel.add(balanceButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(transferButton);
        panel.add(historyButton);

        // Balance Enquiry
        balanceButton.addActionListener(e -> {

            double balance = accountDAO.getBalance(accountNumber);

            if (balance >= 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Current Balance: Rs. " + balance
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Unable to retrieve balance."
                );
            }
        });

        // Deposit Money
        depositButton.addActionListener(e -> {

            String input = JOptionPane.showInputDialog(
                this, "Enter deposit amount:"
            );

            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);

                    boolean result = accountDAO.deposit(
                        accountNumber, amount
                    );

                    if (result) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Deposit successful!\nCurrent Balance: Rs. "
                            + accountDAO.getBalance(accountNumber)
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Deposit failed! Enter a valid amount."
                        );
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        this, "Please enter a valid number."
                    );
                }
            }
        });

        // Withdraw Money
        withdrawButton.addActionListener(e -> {

            String input = JOptionPane.showInputDialog(
                this, "Enter withdrawal amount:"
            );

            if (input != null) {
                try {
                    double amount = Double.parseDouble(input);

                    boolean result = accountDAO.withdraw(
                        accountNumber, amount
                    );

                    if (result) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Withdrawal successful!\nRemaining Balance: Rs. "
                            + accountDAO.getBalance(accountNumber)
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Withdrawal failed! Check the amount and available balance."
                        );
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        this, "Please enter a valid number."
                    );
                }
            }
        });

        // Fund Transfer
        transferButton.addActionListener(e -> {

            String accountInput = JOptionPane.showInputDialog(
                this, "Enter receiver account number:"
            );

            if (accountInput != null) {
                try {
                    int receiverAccount = Integer.parseInt(accountInput);

                    String amountInput = JOptionPane.showInputDialog(
                        this, "Enter transfer amount:"
                    );

                    if (amountInput != null) {

                        double amount = Double.parseDouble(amountInput);

                        boolean result = accountDAO.transfer(
                            accountNumber, receiverAccount, amount
                        );

                        if (result) {
                            JOptionPane.showMessageDialog(
                                this,
                                "Transfer successful!\nRemaining Balance: Rs. "
                                + accountDAO.getBalance(accountNumber)
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                this,
                                "Transfer failed! Check account number, amount and balance."
                            );
                        }
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid account number and amount."
                    );
                }
            }
        });

        // Transaction History
        historyButton.addActionListener(e -> {

            ArrayList<String> history =
                accountDAO.getTransactionHistory(accountNumber);

            StringBuilder result = new StringBuilder();

            for (String transaction : history) {
                result.append(transaction).append("\n\n");
            }

            if (history.isEmpty()) {
                result.append("No transactions found.");
            }

            JTextArea textArea = new JTextArea(result.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(650, 300));

            JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Transaction History",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        add(panel);
        setVisible(true);
    }
}
