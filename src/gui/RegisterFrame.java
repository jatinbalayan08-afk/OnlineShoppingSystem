
package gui;

import dao.CustomerDAO;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;

    private final Color NAVY = new Color(20, 35, 65);
    private final Color BLUE = new Color(48, 105, 220);
    private final Color LIGHT_BG = new Color(242, 246, 253);
    private final Color MUTED = new Color(115, 125, 145);

    public RegisterFrame() {

        setTitle("ShopEase - Create Account");
        setSize(950, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

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
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heading = new JLabel(
                "<html>Join ShopEase<br>Today!</html>"
        );
        heading.setFont(new Font("Segoe UI", Font.BOLD, 29));
        heading.setForeground(Color.WHITE);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Create your account and discover<br>"
                + "a simple and convenient shopping<br>"
                + "experience.</html>"
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

    // RIGHT REGISTRATION PANEL
    private JPanel createRightPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_BG);

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 38, 30, 38));
        card.setPreferredSize(new Dimension(390, 570));

        JLabel heading = new JLabel("Create Account");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(NAVY);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign up to get started with ShopEase");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = new JTextField();
        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();

        styleField(nameField);
        styleField(usernameField);
        styleField(emailField);
        styleField(passwordField);

        showPassword = new JCheckBox("Show password");
        showPassword.setBackground(Color.WHITE);
        showPassword.setForeground(MUTED);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(
                    showPassword.isSelected() ? (char) 0 : '•'
            );
        });

        JButton registerButton =
                UITheme.primaryButton("Create Account");

        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );
        registerButton.setPreferredSize(new Dimension(300, 45));

        registerButton.addActionListener(e -> register());

        JLabel loginText = new JLabel("Already have an account?");
        loginText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginText.setForeground(MUTED);
        loginText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton =
                UITheme.secondaryButton("Back to Login");

        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );
        loginButton.setPreferredSize(new Dimension(300, 42));

        loginButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        card.add(heading);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);

        card.add(Box.createVerticalStrut(23));

        addField(card, "Full Name", nameField);
        addField(card, "Username", usernameField);
        addField(card, "Email Address", emailField);
        addField(card, "Password", passwordField);

        card.add(showPassword);

        card.add(Box.createVerticalStrut(18));
        card.add(registerButton);

        card.add(Box.createVerticalStrut(17));
        card.add(loginText);

        card.add(Box.createVerticalStrut(8));
        card.add(loginButton);

        panel.add(card);

        return panel;
    }

    private void addField(
            JPanel panel,
            String labelText,
            JTextField field
    ) {

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(NAVY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(13));
    }

    private void styleField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 40));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        field.setBackground(Color.WHITE);
        field.setForeground(NAVY);
        field.setCaretColor(BLUE);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(215, 223, 235)
                        ),
                        new EmptyBorder(7, 12, 7, 12)
                )
        );

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // REGISTRATION FUNCTIONALITY
    private void register() {

        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        char[] passwordChars = passwordField.getPassword();

        if (name.isEmpty() || username.isEmpty()
                || email.isEmpty() || passwordChars.length == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields."
            );

            Arrays.fill(passwordChars, '\0');
            return;
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address."
            );

            Arrays.fill(passwordChars, '\0');
            return;
        }

        if (passwordChars.length < 6) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 6 characters."
            );

            Arrays.fill(passwordChars, '\0');
            return;
        }

        String password = new String(passwordChars);
        Arrays.fill(passwordChars, '\0');

        try {

            CustomerDAO dao = new CustomerDAO();

            if (dao.registerCustomer(
                    name, username, email, password
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                nameField.setText("");
                usernameField.setText("");
                emailField.setText("");
                passwordField.setText("");

                showPassword.setSelected(false);
                passwordField.setEchoChar('•');
            }

        } catch (SQLException ex) {

            if (ex.getErrorCode() == 1062) {

                JOptionPane.showMessageDialog(
                        this,
                        "Username or email already exists."
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed: " + ex.getMessage(),
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } finally {
            password = null;
        }
    }

    public static void main(String[] args) {

        UITheme.apply();

        SwingUtilities.invokeLater(() -> {
            new RegisterFrame().setVisible(true);
        });
    }
}