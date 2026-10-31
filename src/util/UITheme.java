package util;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UITheme {

    public static final Color PRIMARY = new Color(31, 78, 121);
    public static final Color PRIMARY_DARK = new Color(20, 52, 85);
    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Color WHITE = Color.WHITE;
    public static final Color SURFACE = WHITE;
    public static final Color TEXT = new Color(40, 50, 65);
    public static final Color MUTED = new Color(120, 130, 145);
    public static final Color SUCCESS = new Color(34, 139, 94);
    public static final Color DANGER = new Color(210, 65, 65);
    public static final Color BORDER = new Color(225, 230, 238);

    public static void apply() {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Table.rowHeight", 32);
    }

    // New button methods

    public static JButton createButton(String text) {

    JButton button = new JButton(text);

    button.setUI(new BasicButtonUI());
    button.setFont(new Font("Segoe UI", Font.BOLD, 13));

    button.setBackground(PRIMARY);
    button.setForeground(Color.WHITE);

    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorderPainted(false);
    button.setFocusPainted(false);

    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    button.setBorder(new EmptyBorder(11, 20, 11, 20));

    button.setRolloverEnabled(true);

    button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
            button.setBackground(PRIMARY_DARK);
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
            button.setBackground(PRIMARY);
        }
    });

    return button;
}

public static JButton createDangerButton(String text) {

    JButton button = createButton(text);
    button.setBackground(DANGER);

    return button;
}

public static JButton primaryButton(String text) {
    return createButton(text);
}

public static JButton secondaryButton(String text) {

    JButton button = createButton(text);

    button.setBackground(new Color(235, 241, 248));
    button.setForeground(PRIMARY);

    button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
            button.setBackground(new Color(215, 230, 245));
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
            button.setBackground(new Color(235, 241, 248));
        }
    });

    return button;
}

    public static JLabel heading(String text) {
        return createTitle(text);
    }

    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 25));
        label.setForeground(PRIMARY_DARK);
        return label;
    }

    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(18, 18, 18, 18)
        ));
        return panel;
    }

    public static void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(210, 218, 228), 1, true),
            new EmptyBorder(9, 12, 9, 12)
        ));
    }

    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(220, 235, 250));
        table.setSelectionForeground(TEXT);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );

        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(WHITE);
    }
}