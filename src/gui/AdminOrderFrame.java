package gui;

import dao.AdminOrderDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminOrderFrame extends JFrame {

    private final AdminOrderDAO orderDAO = new AdminOrderDAO();

    private JTable orderTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;
    private JComboBox<String> statusBox;

    public AdminOrderFrame() {

        setTitle("ShopEase - Order Management");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadOrders("");
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(new Color(35, 45, 65));
        header.setBorder(
                BorderFactory.createEmptyBorder(18, 25, 18, 25)
        );

        JLabel title = new JLabel("Order Management");

        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout(10, 15));

        main.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Search Panel
        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        searchField = new JTextField(20);

        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        searchButton.addActionListener(e ->
                loadOrders(searchField.getText())
        );

        searchField.addActionListener(e ->
                loadOrders(searchField.getText())
        );

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadOrders("");
        });

        searchPanel.add(new JLabel("Customer Username:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        // Orders Table
        String[] columns = {
            "Order ID",
            "Customer",
            "Amount",
            "Order Date",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(28);

        orderTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane tableScroll = new JScrollPane(orderTable);

        tableScroll.setBorder(
                BorderFactory.createTitledBorder("All Orders")
        );

        // Action Panel
        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 12)
        );

        JButton detailsButton = new JButton("View Details");
        JButton updateButton = new JButton("Update Status");
        JButton cancelButton = new JButton("Cancel Order");

        statusBox = new JComboBox<>(new String[]{
            "PLACED",
            "SHIPPED",
            "DELIVERED",
            "CANCELLED"
        });

        detailsButton.addActionListener(e -> viewDetails());

        updateButton.addActionListener(e -> updateStatus());

        cancelButton.addActionListener(e -> cancelOrder());

        actionPanel.add(new JLabel("New Status:"));
        actionPanel.add(statusBox);
        actionPanel.add(updateButton);
        actionPanel.add(detailsButton);
        actionPanel.add(cancelButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(actionPanel, BorderLayout.CENTER);

        main.add(searchPanel, BorderLayout.NORTH);
        main.add(tableScroll, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.SOUTH);

        return main;
    }

    // Load orders from database
    private void loadOrders(String search) {

        tableModel.setRowCount(0);

        List<Object[]> orders = orderDAO.getAllOrders(search);

        for (Object[] order : orders) {
            tableModel.addRow(order);
        }
    }

    // Get selected order ID
    private int getSelectedOrderId() {

        int row = orderTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an order first."
            );

            return -1;
        }

        return Integer.parseInt(
                tableModel.getValueAt(row, 0).toString()
        );
    }

    // View order details
    private void viewDetails() {

        int orderId = getSelectedOrderId();

        if (orderId == -1) {
            return;
        }

        List<Object[]> items =
                orderDAO.getOrderDetails(orderId);

        String[] columns = {
            "Product",
            "Price",
            "Quantity",
            "Subtotal"
        };

        DefaultTableModel detailsModel =
                new DefaultTableModel(columns, 0);

        for (Object[] item : items) {
            detailsModel.addRow(item);
        }

        JTable detailsTable = new JTable(detailsModel);

        detailsTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(detailsTable);

        scrollPane.setPreferredSize(new Dimension(550, 250));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Order Details - #" + orderId,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Update order status
    private void updateStatus() {

        int orderId = getSelectedOrderId();

        if (orderId == -1) {
            return;
        }

        String status = statusBox.getSelectedItem().toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Update Order #" + orderId
                        + " status to " + status + "?",
                "Confirm Status Update",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            boolean success =
                    orderDAO.updateOrderStatus(orderId, status);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Order status updated successfully!"
                );

                loadOrders(searchField.getText());

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to update order status."
                );
            }
        }
    }

    // Cancel order
    private void cancelOrder() {

        int orderId = getSelectedOrderId();

        if (orderId == -1) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel Order #"
                        + orderId + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            boolean success =
                    orderDAO.updateOrderStatus(
                            orderId, "CANCELLED"
                    );

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Order cancelled successfully!"
                );

                loadOrders(searchField.getText());

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to cancel order."
                );
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new AdminOrderFrame().setVisible(true)
        );
    }
}