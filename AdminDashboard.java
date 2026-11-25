
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class AdminDashboard extends JFrame {

    private AccountDAO accountDAO;

    public AdminDashboard() {

        accountDAO = new AccountDAO();

        setTitle("Admin Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 15));
        panel.setBackground(new Color(230, 240, 250));

        JLabel title = new JLabel(
            "ADMIN DASHBOARD",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton customersButton = new JButton("View All Customers");
        JButton transactionsButton = new JButton("View All Transactions");
        JButton manageButton = new JButton("Manage Accounts");
        JButton balanceButton = new JButton("Total Bank Balance");
        JButton logoutButton = new JButton("Logout");

        panel.add(title);
        panel.add(customersButton);
        panel.add(transactionsButton);
        panel.add(manageButton);
        panel.add(balanceButton);
        panel.add(logoutButton);

        // View All Customers
        customersButton.addActionListener(e -> {

            ArrayList<String> customers =
                accountDAO.getAllCustomers();

            StringBuilder result = new StringBuilder();

            for (String customer : customers) {
                result.append(customer).append("\n\n");
            }

            if (customers.isEmpty()) {
                result.append("No customers found.");
            }

            JTextArea textArea = new JTextArea(result.toString());
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(650, 300));

            JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "All Customers",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        // View All Transactions
        transactionsButton.addActionListener(e -> {

            ArrayList<String> transactions =
                accountDAO.getAllTransactions();

            StringBuilder result = new StringBuilder();

            for (String transaction : transactions) {

                result.append(transaction).append("\n\n");
                result.append("--------------------------------\n\n");
            }

            if (transactions.isEmpty()) {
                result.append("No transactions found.");
            }

            JTextArea textArea = new JTextArea(result.toString());
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(650, 350));

            JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "All Transactions",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Manage Accounts
        manageButton.addActionListener(e -> {

            String[] options = {
                "Add Customer Account",
                "Delete Customer Account",
                "Cancel"
            };

            int choice = JOptionPane.showOptionDialog(
                this,
                "Choose an account management operation:",
                "Manage Accounts",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            // Add Customer Account
            if (choice == 0) {

                JTextField accountField = new JTextField();
                JTextField nameField = new JTextField();
                JTextField balanceField = new JTextField();

                Object[] fields = {
                    "Enter Account Number:", accountField,
                    "Enter Customer Name:", nameField,
                    "Enter Initial Balance:", balanceField
                };

                int option = JOptionPane.showConfirmDialog(
                    this,
                    fields,
                    "Add New Customer Account",
                    JOptionPane.OK_CANCEL_OPTION
                );

                if (option == JOptionPane.OK_OPTION) {

                    try {

                        int accountNumber =
                            Integer.parseInt(accountField.getText().trim());

                        String customerName =
                            nameField.getText().trim();

                        double balance =
                            Double.parseDouble(balanceField.getText().trim());

                        boolean success = accountDAO.addAccount(
                            accountNumber,
                            customerName,
                            balance
                        );

                        if (success) {
                            JOptionPane.showMessageDialog(
                                this,
                                "Customer account created successfully!"
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                this,
                                "Account creation failed. Check the details or account number."
                            );
                        }

                    } catch (NumberFormatException ex) {

                        JOptionPane.showMessageDialog(
                            this,
                            "Please enter valid numbers for account number and balance."
                        );
                    }
                }
            }

            // Delete Customer Account
            else if (choice == 1) {

                String accountInput = JOptionPane.showInputDialog(
                    this,
                    "Enter the account number to delete:"
                );

                if (accountInput != null) {

                    try {

                        int accountNumber =
                            Integer.parseInt(accountInput.trim());

                        int confirmation = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete account "
                                + accountNumber + "?",
                            "Confirm Account Deletion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                        );

                        if (confirmation == JOptionPane.YES_OPTION) {

                            boolean success =
                                accountDAO.deleteAccount(accountNumber);

                            if (success) {
                                JOptionPane.showMessageDialog(
                                    this,
                                    "Customer account deleted successfully!"
                                );
                            } else {
                                JOptionPane.showMessageDialog(
                                    this,
                                    "Account could not be deleted. It may not exist or may have transaction records."
                                );
                            }
                        }

                    } catch (NumberFormatException ex) {

                        JOptionPane.showMessageDialog(
                            this,
                            "Please enter a valid account number."
                        );
                    }
                }
            }
        });

        // Total Bank Balance
        balanceButton.addActionListener(e -> {

            double totalBalance =
                accountDAO.getTotalBankBalance();

            JOptionPane.showMessageDialog(
                this,
                "Total Bank Balance: Rs. " + totalBalance,
                "Bank Balance",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Logout
        logoutButton.addActionListener(e -> {
            dispose();
            new Main();
        });

        add(panel);
        setVisible(true);
    }
}
