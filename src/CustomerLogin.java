
import java.awt.*;
import javax.swing.*;

public class CustomerLogin extends JFrame {

    private AccountDAO accountDAO;

    public CustomerLogin() {

        accountDAO = new AccountDAO();

        setTitle("Customer Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15));

        JLabel title = new JLabel("Customer Login");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Close");

        panel.add(title);
        panel.add(new JLabel(""));

        panel.add(usernameLabel);
        panel.add(usernameField);

        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(loginButton);
        panel.add(backButton);

        // Customer Login Authentication
        loginButton.addActionListener(e -> {

            String username = usernameField.getText().trim();

            String password =
                new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
                );

                return;
            }

            int accountNumber =
                accountDAO.authenticateCustomer(username, password);

            if (accountNumber != -1) {

                JOptionPane.showMessageDialog(
                    this,
                    "Login Successful!"
                );

                new CustomerDashboard(accountNumber);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password!"
                );
            }
        });

        // Close Login Window
        backButton.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }
}
