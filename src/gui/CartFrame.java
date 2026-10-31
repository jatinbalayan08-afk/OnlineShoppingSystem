package gui;

import dao.OrderDAO;
import dao.OrderDAO.OrderItem;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CartFrame extends JFrame {

    private final DefaultTableModel model;
    private final JLabel totalLabel;
    private final JLabel itemCountLabel;
    private final String username;

    private JTable cartTable;

    public CartFrame(String username) {

        this.username = username;

        setTitle("ShopEase | Shopping Cart");
        setSize(950, 650);
        setMinimumSize(new Dimension(750, 500));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        // Table Model

        String[] columns = {
            "Product", "Price (₹)", "Quantity", "Subtotal (₹)"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Header

        add(createHeader(), BorderLayout.NORTH);

        // Main content

        add(createMainContent(), BorderLayout.CENTER);

        // Bottom summary

        totalLabel = new JLabel("₹0.00");
        itemCountLabel = new JLabel("0 items");

        add(createBottomPanel(), BorderLayout.SOUTH);

        updateTotal();
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(UITheme.WHITE);

        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UITheme.BORDER),
            new EmptyBorder(18, 30, 18, 30)
        ));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(
            titlePanel, BoxLayout.Y_AXIS
        ));

        JLabel brand = new JLabel("ShopEase");

        brand.setFont(new Font(
            "Segoe UI", Font.BOLD, 25
        ));

        brand.setForeground(UITheme.PRIMARY_DARK);

        JLabel subtitle = new JLabel("YOUR SHOPPING CART");

        subtitle.setFont(new Font(
            "Segoe UI", Font.PLAIN, 10
        ));

        subtitle.setForeground(UITheme.MUTED);

        titlePanel.add(brand);
        titlePanel.add(subtitle);

        JLabel welcome = new JLabel("Welcome, " + username);

        welcome.setFont(new Font(
            "Segoe UI", Font.BOLD, 13
        ));

        welcome.setForeground(UITheme.TEXT);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        return header;
    }

    // ================= MAIN CONTENT =================

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(15, 20));

        main.setBackground(UITheme.BACKGROUND);

        main.setBorder(new EmptyBorder(25, 30, 20, 30));

        // Heading

        JPanel headingPanel = new JPanel();

        headingPanel.setOpaque(false);

        headingPanel.setLayout(new BoxLayout(
            headingPanel, BoxLayout.Y_AXIS
        ));

        JLabel heading = UITheme.createTitle("Shopping Cart");

        JLabel subtitle = new JLabel(
            "Review your selected products before checkout."
        );

        subtitle.setFont(new Font(
            "Segoe UI", Font.PLAIN, 14
        ));

        subtitle.setForeground(UITheme.MUTED);

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(7));
        headingPanel.add(subtitle);

        // Table

        cartTable = new JTable(model);

        UITheme.styleTable(cartTable);

        cartTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        cartTable.setIntercellSpacing(new Dimension(0, 5));

        cartTable.setBorder(BorderFactory.createEmptyBorder());

        cartTable.getColumnModel().getColumn(0)
            .setPreferredWidth(250);

        cartTable.getColumnModel().getColumn(1)
            .setPreferredWidth(130);

        cartTable.getColumnModel().getColumn(2)
            .setPreferredWidth(110);

        cartTable.getColumnModel().getColumn(3)
            .setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(cartTable);

        scrollPane.setBorder(new LineBorder(
            UITheme.BORDER, 1, true
        ));

        scrollPane.getViewport().setBackground(UITheme.WHITE);

        // Cart card

        JPanel tableCard = new JPanel(new BorderLayout());

        tableCard.setBackground(UITheme.WHITE);

        tableCard.setBorder(new EmptyBorder(12, 12, 12, 12));

        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Remove button

        JButton removeButton =
            UITheme.createDangerButton("Remove Selected Item");

        removeButton.addActionListener(e -> removeSelectedItem());

        JPanel actionPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 0, 12)
        );

        actionPanel.setOpaque(false);

        actionPanel.add(removeButton);

        tableCard.add(actionPanel, BorderLayout.SOUTH);

        main.add(headingPanel, BorderLayout.NORTH);
        main.add(tableCard, BorderLayout.CENTER);

        return main;
    }

    // ================= BOTTOM SUMMARY =================

    private JPanel createBottomPanel() {

        JPanel bottom = new JPanel(new BorderLayout());

        bottom.setBackground(UITheme.WHITE);

        bottom.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, UITheme.BORDER),
            new EmptyBorder(18, 30, 18, 30)
        ));

        // Item information

        JPanel infoPanel = new JPanel();

        infoPanel.setOpaque(false);

        infoPanel.setLayout(new BoxLayout(
            infoPanel, BoxLayout.Y_AXIS
        ));

        JLabel itemsTitle = new JLabel("Cart Summary");

        itemsTitle.setFont(new Font(
            "Segoe UI", Font.BOLD, 16
        ));

        itemsTitle.setForeground(UITheme.TEXT);

        itemCountLabel.setFont(new Font(
            "Segoe UI", Font.PLAIN, 13
        ));

        itemCountLabel.setForeground(UITheme.MUTED);

        infoPanel.add(itemsTitle);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(itemCountLabel);

        // Total information

        JPanel totalPanel = new JPanel();

        totalPanel.setOpaque(false);

        totalPanel.setLayout(new BoxLayout(
            totalPanel, BoxLayout.Y_AXIS
        ));

        JLabel totalTitle = new JLabel("Grand Total");

        totalTitle.setFont(new Font(
            "Segoe UI", Font.PLAIN, 13
        ));

        totalTitle.setForeground(UITheme.MUTED);

        totalLabel.setFont(new Font(
            "Segoe UI", Font.BOLD, 24
        ));

        totalLabel.setForeground(UITheme.PRIMARY);

        totalPanel.add(totalTitle);
        totalPanel.add(Box.createVerticalStrut(4));
        totalPanel.add(totalLabel);

        // Buttons

        JButton checkoutButton =
            UITheme.createButton("Proceed to Checkout");

        checkoutButton.setPreferredSize(
            new Dimension(210, 45)
        );

        checkoutButton.addActionListener(e -> checkout());

        JPanel rightPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 20, 0)
        );

        rightPanel.setOpaque(false);

        rightPanel.add(totalPanel);
        rightPanel.add(checkoutButton);

        bottom.add(infoPanel, BorderLayout.WEST);
        bottom.add(rightPanel, BorderLayout.EAST);

        return bottom;
    }

    // ================= ADD PRODUCT =================

    public void addProduct(String name, double price) {

        for (int i = 0; i < model.getRowCount(); i++) {

            if (model.getValueAt(i, 0).equals(name)) {

                int quantity = Integer.parseInt(
                    model.getValueAt(i, 2).toString()
                ) + 1;

                model.setValueAt(quantity, i, 2);

                model.setValueAt(
                    price * quantity, i, 3
                );

                updateTotal();

                return;
            }
        }

        model.addRow(new Object[]{
            name, price, 1, price
        });

        updateTotal();
    }

    // ================= REMOVE PRODUCT =================

    private void removeSelectedItem() {

        int row = cartTable.getSelectedRow();

        if (row < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Please select an item to remove.",
                "No Item Selected",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Remove this product from your cart?",
            "Confirm Removal",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {

            model.removeRow(row);

            updateTotal();
        }
    }

    // ================= UPDATE TOTAL =================

    private void updateTotal() {

        double total = 0;

        int totalItems = 0;

        for (int i = 0; i < model.getRowCount(); i++) {

            total += Double.parseDouble(
                model.getValueAt(i, 3).toString()
            );

            totalItems += Integer.parseInt(
                model.getValueAt(i, 2).toString()
            );
        }

        totalLabel.setText(
            String.format("\u20B9%.2f", total)
        );

        itemCountLabel.setText(
            totalItems + (totalItems == 1 ? " item" : " items")
        );
    }

    // ================= CHECKOUT =================

    private void checkout() {

        if (model.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                this,
                "Your cart is empty.",
                "Empty Cart",
                JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Confirm your order?\n" + totalLabel.getText(),
            "Confirm Checkout",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        List<OrderItem> items = new ArrayList<>();

        double total = 0;

        for (int i = 0; i < model.getRowCount(); i++) {

            String name = model.getValueAt(i, 0).toString();

            double price = Double.parseDouble(
                model.getValueAt(i, 1).toString()
            );

            int quantity = Integer.parseInt(
                model.getValueAt(i, 2).toString()
            );

            double subtotal = Double.parseDouble(
                model.getValueAt(i, 3).toString()
            );

            items.add(new OrderItem(
                name, price, quantity, subtotal
            ));

            total += subtotal;
        }

        OrderDAO orderDAO = new OrderDAO();

        int orderId = orderDAO.placeOrder(
            username, items, total
        );

        if (orderId > 0) {

            model.setRowCount(0);

            updateTotal();

            JOptionPane.showMessageDialog(
                this,
                "Order placed successfully!\n\n"
                    + "Order ID: " + orderId + "\n"
                    + String.format(
                        "Total Amount: \u20B9%.2f", total
                    ),
                "Order Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            setVisible(false);

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Order could not be placed. Please check "
                    + "your database connection and try again.",
                "Checkout Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}