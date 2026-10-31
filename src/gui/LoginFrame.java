package gui;

import dao.LoginDAO;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;

    private final Color NAVY = new Color(20, 35, 65);
    private final Color BLUE = new Color(48, 105, 220);
    private final Color LIGHT_BG = new Color(242, 246, 253);
    private final Color MUTED = new Color(115, 125, 145);

    public LoginFrame() {

        setTitle("ShopEase - Login");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createLeftPanel());
        mainPanel.add(createRightPanel());

        add(mainPanel);
    }

    // LEFT BRANDING PANEL
    private JPanel createLeftPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(NAVY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(80, 55, 60, 55));

        JLabel logo = new JLabel("ShopEase");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("Your Shopping, Simplified.");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        tagline.setForeground(new Color(190, 205, 230));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel icon = new JLabel("🛍");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 85));
        icon.setForeground(Color.WHITE);
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heading = new JLabel(
                "<html>Everything you need,<br>in one place.</html>"
        );

        heading.setFont(new Font("Segoe UI", Font.BOLD, 27));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Discover products, manage your cart,<br>"
                + "and enjoy a simple shopping experience.</html>"
        );

        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setForeground(new Color(190, 205, 230));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(logo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tagline);
        panel.add(Box.createVerticalGlue());
        panel.add(icon);
        panel.add(Box.createVerticalStrut(20));
        panel.add(heading);
        panel.add(Box.createVerticalStrut(15));
        panel.add(description);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // RIGHT LOGIN PANEL
    private JPanel createRightPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_BG);

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 38, 40, 38));
        card.setPreferredSize(new Dimension(370, 440));

        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 29));
        welcome.setForeground(NAVY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Login to your ShopEase account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel usernameLabel = createLabel("Username");

        usernameField = new JTextField();
        styleField(usernameField);

        JLabel passwordLabel = createLabel("Password");

        passwordField = new JPasswordField();
        styleField(passwordField);

        showPassword = new JCheckBox("Show password");
        showPassword.setBackground(Color.WHITE);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword.setForeground(MUTED);
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(
                    showPassword.isSelected() ? (char) 0 : '•'
            );
        });

        JButton loginButton = UITheme.primaryButton("Login");

        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setPreferredSize(new Dimension(290, 45));
        loginButton.addActionListener(e -> login());

        JLabel registerText = new JLabel(
                "Don't have an account?"
        );

        registerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerText.setForeground(MUTED);
        registerText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerButton = UITheme.secondaryButton("Create Account");

        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        registerButton.setPreferredSize(new Dimension(290, 42));

        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
JButton adminButton = UITheme.secondaryButton("Login as Admin");

adminButton.setAlignmentX(Component.LEFT_ALIGNMENT);
adminButton.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 42)
);
adminButton.setPreferredSize(new Dimension(290, 42));

adminButton.addActionListener(e -> {
    new AdminLoginFrame().setVisible(true);
    dispose();
});
        card.add(welcome);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
card.add(Box.createVerticalStrut(10));
card.add(adminButton);
        card.add(Box.createVerticalStrut(35));

        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(usernameField);

        card.add(Box.createVerticalStrut(22));

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(passwordField);

        card.add(Box.createVerticalStrut(8));
        card.add(showPassword);

        card.add(Box.createVerticalStrut(25));
        card.add(loginButton);

        card.add(Box.createVerticalStrut(22));
        card.add(registerText);

        card.add(Box.createVerticalStrut(10));
        card.add(registerButton);

        panel.add(card);

        return panel;
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(NAVY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private void styleField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(290, 43));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));

        field.setBackground(Color.WHITE);
        field.setForeground(NAVY);
        field.setCaretColor(BLUE);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 223, 235)
                        ),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // LOGIN FUNCTIONALITY
    private void login() {

        String username = usernameField.getText().trim();

        char[] passwordChars = passwordField.getPassword();

        if (username.isEmpty() || passwordChars.length == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password."
            );

            Arrays.fill(passwordChars, '\0');
            return;
        }

        String password = new String(passwordChars);
        Arrays.fill(passwordChars, '\0');

        try {

            LoginDAO dao = new LoginDAO();

            if (dao.authenticate(username, password)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login successful!"
                );

                dispose();

                new ProductFrame(username).setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + ex.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {
            password = null;
        }
    }

    public static void main(String[] args) {

        UITheme.apply();

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}