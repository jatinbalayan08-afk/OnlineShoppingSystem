package gui;

import dao.AdminDAO;
import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLoginFrame() {

        setTitle("ShopEase - Admin Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel heading = new JLabel("ADMIN LOGIN", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        panel.add(heading);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        add(panel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> loginAdmin());
    }

    private void loginAdmin() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        AdminDAO dao = new AdminDAO();

        if (dao.login(username, password)) {

            JOptionPane.showMessageDialog(
                    this, "Admin Login Successful!"
            );

            new AdminDashboard().setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(
                    this, "Invalid username or password!"
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new AdminLoginFrame().setVisible(true)
        );
    }
}