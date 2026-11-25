
import java.awt.*;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Banking Transaction System");

        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 20));
        panel.setBackground(new Color(230, 240, 250));

        // Bank heading
        JLabel title = new JLabel(
            "ABC BANK",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 32));

        // Welcome message
        JLabel welcome = new JLabel(
            "Welcome to Banking Transaction System",
            SwingConstants.CENTER
        );

        welcome.setFont(new Font("Arial", Font.PLAIN, 20));

        // Login buttons
        JButton customerButton = new JButton("Customer Login");
        JButton adminButton = new JButton("Admin Login");

        customerButton.setFont(new Font("Arial", Font.BOLD, 16));
        adminButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Customer Login button action
        customerButton.addActionListener(e -> {
            new CustomerLogin();
        });

        // Admin Login button action
        adminButton.addActionListener(e -> {
            new AdminLogin();
        });

        // Add components
        panel.add(title);
        panel.add(welcome);
        panel.add(customerButton);
        panel.add(adminButton);

        frame.add(panel);

        frame.setVisible(true);
    }
}
