package gui;

import dao.AdminCustomerDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminCustomerFrame extends JFrame {

    private final AdminCustomerDAO customerDAO =
            new AdminCustomerDAO();

    private JTable customerTable;
    private DefaultTableModel customerModel;

    private JTextField searchField;

    public AdminCustomerFrame() {

        setTitle("ShopEase - Customer Management");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadCustomers("");
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(new Color(35, 45, 65));

        header.setBorder(
                BorderFactory.createEmptyBorder(18, 25, 18, 25)
        );

        JLabel title = new JLabel("Customer Management");

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

        searchField = new JTextField(22);

        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        searchButton.addActionListener(e ->
                loadCustomers(searchField.getText())
        );

        searchField.addActionListener(e ->
                loadCustomers(searchField.getText())
        );

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadCustomers("");
        });

        searchPanel.add(new JLabel("Search Customer:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        // Customer Table
        String[] columns = {
            "Full Name",
            "Username",
            "Email"
        };

        customerModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        customerTable = new JTable(customerModel);

        customerTable.setRowHeight(30);

        customerTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane = new JScrollPane(customerTable);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Registered Customers")
        );

        // Buttons
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 12)
        );

        JButton detailsButton = new JButton("Customer Details");
        JButton historyButton = new JButton("Order History");

        detailsButton.addActionListener(e -> viewCustomerDetails());

        historyButton.addActionListener(e -> viewOrderHistory());

        buttonPanel.add(detailsButton);
        buttonPanel.add(historyButton);

        main.add(searchPanel, BorderLayout.NORTH);
        main.add(scrollPane, BorderLayout.CENTER);
        main.add(buttonPanel, BorderLayout.SOUTH);

        return main;
    }

    // Load customers
    private void loadCustomers(String search) {

        customerModel.setRowCount(0);

        List<Object[]> customers =
                customerDAO.getAllCustomers(search);

        for (Object[] customer : customers) {
            customerModel.addRow(customer);
        }
    }

    // Get selected customer
    private int getSelectedRow() {

        int row = customerTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a customer first."
            );

            return -1;
        }

        return row;
    }

    // Customer details
    private void viewCustomerDetails() {

        int row = getSelectedRow();

        if (row == -1) {
            return;
        }

        String name = customerModel.getValueAt(row, 0).toString();
        String username = customerModel.getValueAt(row, 1).toString();
        String email = customerModel.getValueAt(row, 2).toString();

        String details =
                "CUSTOMER DETAILS\n\n"
                + "Name: " + name + "\n"
                + "Username: " + username + "\n"
                + "Email: " + email;

        JOptionPane.showMessageDialog(
                this,
                details,
                "Customer Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Customer order history
    private void viewOrderHistory() {

        int row = getSelectedRow();

        if (row == -1) {
            return;
        }

        String username =
                customerModel.getValueAt(row, 1).toString();

        List<Object[]> orders =
                customerDAO.getCustomerOrders(username);

        String[] columns = {
            "Order ID",
            "Total Amount",
            "Order Date",
            "Status"
        };

        DefaultTableModel orderModel =
                new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Object[] order : orders) {
            orderModel.addRow(order);
        }

        JTable orderTable = new JTable(orderModel);

        orderTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(orderTable);

        scrollPane.setPreferredSize(
                new Dimension(650, 250)
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Order History - " + username,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new AdminCustomerFrame().setVisible(true)
        );
    }
}