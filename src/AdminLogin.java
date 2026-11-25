
import java.awt.*;
import javax.swing.*;

public class AdminLogin extends JFrame {

    private AccountDAO accountDAO;

    public AdminLogin() {

        accountDAO = new AccountDAO();

        setTitle("Admin Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15));

        JLabel title = new JLabel("Admin Login");
        JLabel usernameLabel = new JLabel("Admin Username:");
        JLabel passwordLabel = new JLabel("Admin Password:");

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

        // Admin Login Authentication
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

            boolean isAdmin =
                accountDAO.authenticateAdmin(username, password);

            if (isAdmin) {

                JOptionPane.showMessageDialog(
                    this,
                    "Admin Login Successful!"
                );

                new AdminDashboard();

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid Admin Username or Password!"
                );
            }
        });

        // Close Admin Login Window
        backButton.addActionListener(e -> dispose());

        add(panel);
        setVisible(true);
    }
}
