package gui;

import dao.ProductDAO;
import dao.DashboardDAO;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private JTextField nameField, categoryField;
    private JTextField priceField, stockField;
    private JTextArea descriptionArea;

    private JTable productTable, orderTable;

    private DefaultTableModel tableModel, orderModel;

    private final ProductDAO productDAO = new ProductDAO();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    private JLabel productsValue;
    private JLabel customersValue;
    private JLabel ordersValue;
    private JLabel salesValue;

    public AdminDashboard() {

        UITheme.apply();

        setTitle("ShopEase | Admin Dashboard");
        setSize(1300, 900);
        setMinimumSize(new Dimension(1050, 750));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(UITheme.BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        mainPanel.add(createAnalyticsPanel(), BorderLayout.NORTH);
        mainPanel.add(createManagementPanel(), BorderLayout.CENTER);

        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(BorderFactory.createEmptyBorder());
        mainScroll.getViewport().setBackground(UITheme.BACKGROUND);
        mainScroll.getVerticalScrollBar().setUnitIncrement(18);

        add(mainScroll, BorderLayout.CENTER);

        loadProducts();
        loadAnalytics();
        loadRecentOrders();
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout(20, 10));

        header.setBackground(UITheme.PRIMARY_DARK);

        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel brandPanel = new JPanel();

        brandPanel.setOpaque(false);

        brandPanel.setLayout(new BoxLayout(
                brandPanel, BoxLayout.Y_AXIS
        ));

        JLabel title = new JLabel("ShopEase");

        title.setFont(new Font(
                "Segoe UI", Font.BOLD, 28
        ));

        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("ADMIN CONTROL PANEL");

        subtitle.setFont(new Font(
                "Segoe UI", Font.PLAIN, 10
        ));

        subtitle.setForeground(new Color(190, 205, 225));

        brandPanel.add(title);
        brandPanel.add(subtitle);

        JButton refreshButton =
                UITheme.createButton("Refresh Dashboard");

        JButton ordersButton =
                UITheme.secondaryButton("Manage Orders");

        JButton customersButton =
                UITheme.secondaryButton("Manage Customers");

        refreshButton.addActionListener(e -> refreshAll());

        ordersButton.addActionListener(e ->
                new AdminOrderFrame().setVisible(true)
        );

        customersButton.addActionListener(e ->
                new AdminCustomerFrame().setVisible(true)
        );

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 5)
        );

        buttons.setOpaque(false);

        buttons.add(customersButton);
        buttons.add(ordersButton);
        buttons.add(refreshButton);

        header.add(brandPanel, BorderLayout.WEST);
        header.add(buttons, BorderLayout.EAST);

        return header;
    }

    // ================= ANALYTICS =================

    private JPanel createAnalyticsPanel() {

        JPanel panel = new JPanel(
                new GridLayout(1, 4, 18, 18)
        );

        panel.setOpaque(false);

        productsValue = new JLabel("0");
        customersValue = new JLabel("0");
        ordersValue = new JLabel("0");
        salesValue = new JLabel("₹0.00");

        panel.add(createCard(
                "TOTAL PRODUCTS",
                productsValue,
                new Color(47, 128, 237),
                "01"
        ));

        panel.add(createCard(
                "TOTAL CUSTOMERS",
                customersValue,
                new Color(39, 174, 96),
                "02"
        ));

        panel.add(createCard(
                "TOTAL ORDERS",
                ordersValue,
                new Color(142, 68, 173),
                "03"
        ));

        panel.add(createCard(
                "TOTAL SALES",
                salesValue,
                new Color(230, 126, 34),
                "04"
        ));

        return panel;
    }

    private JPanel createCard(
            String title,
            JLabel value,
            Color accent,
            String number
    ) {

        JPanel card = new JPanel(new BorderLayout(10, 12));

        card.setBackground(UITheme.WHITE);

        card.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JPanel top = new JPanel(new BorderLayout());

        top.setOpaque(false);

        JLabel heading = new JLabel(title);

        heading.setFont(new Font(
                "Segoe UI", Font.BOLD, 12
        ));

        heading.setForeground(UITheme.MUTED);

        JLabel badge = new JLabel(number);

        badge.setFont(new Font(
                "Segoe UI", Font.BOLD, 11
        ));

        badge.setForeground(accent);

        top.add(heading, BorderLayout.WEST);
        top.add(badge, BorderLayout.EAST);

        value.setFont(new Font(
                "Segoe UI", Font.BOLD, 29
        ));

        value.setForeground(UITheme.TEXT);

        JPanel bottom = new JPanel(new BorderLayout());

        bottom.setOpaque(false);

        JPanel accentLine = new JPanel();

        accentLine.setBackground(accent);

        accentLine.setPreferredSize(new Dimension(55, 5));

        bottom.add(accentLine, BorderLayout.WEST);

        card.add(top, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    // ================= LOAD ANALYTICS =================

    private void loadAnalytics() {

        productsValue.setText(
                String.valueOf(dashboardDAO.getTotalProducts())
        );

        customersValue.setText(
                String.valueOf(dashboardDAO.getTotalCustomers())
        );

        ordersValue.setText(
                String.valueOf(dashboardDAO.getTotalOrders())
        );

        salesValue.setText(
                String.format("₹%.2f", dashboardDAO.getTotalSales())
        );
    }

    // ================= MANAGEMENT PANEL =================

    private JPanel createManagementPanel() {

        JPanel panel = new JPanel(new BorderLayout(18, 18));

        panel.setOpaque(false);

        panel.add(createProductForm(), BorderLayout.NORTH);
        panel.add(createProductTable(), BorderLayout.CENTER);
        panel.add(createRecentOrders(), BorderLayout.SOUTH);

        return panel;
    }

    // ================= PRODUCT FORM =================

    private JPanel createProductForm() {

        JPanel form = createSectionPanel();

        form.setLayout(new BorderLayout(15, 15));

        JLabel heading = createSectionTitle("Product Management");

        JPanel fields = new JPanel(
                new GridLayout(2, 5, 12, 8)
        );

        fields.setOpaque(false);

        nameField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();
        descriptionArea = new JTextArea(2, 15);

        styleInput(nameField);
        styleInput(categoryField);
        styleInput(priceField);
        styleInput(stockField);

        descriptionArea.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        fields.add(createFieldPanel("Product Name", nameField));
        fields.add(createFieldPanel("Category", categoryField));
        fields.add(createFieldPanel("Price (₹)", priceField));
        fields.add(createFieldPanel("Stock", stockField));
        fields.add(createFieldPanel(
                "Description",
                new JScrollPane(descriptionArea)
        ));

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        buttons.setOpaque(false);

        JButton add = UITheme.createButton("Add Product");

        JButton update = UITheme.createButton("Update Product");

        JButton delete = UITheme.createDangerButton("Delete Product");

        JButton clear = UITheme.secondaryButton("Clear");

        JButton refresh = UITheme.secondaryButton("Refresh");

        add.addActionListener(e -> addProduct());

        update.addActionListener(e -> updateProduct());

        delete.addActionListener(e -> deleteProduct());

        clear.addActionListener(e -> clearFields());

        refresh.addActionListener(e -> refreshAll());

        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(clear);
        buttons.add(refresh);

        form.add(heading, BorderLayout.NORTH);
        form.add(fields, BorderLayout.CENTER);
        form.add(buttons, BorderLayout.SOUTH);

        return form;
    }

    private JPanel createFieldPanel(
            String labelText,
            JComponent input
    ) {

        JPanel panel = new JPanel(new BorderLayout(5, 7));

        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);

        label.setFont(new Font(
                "Segoe UI", Font.BOLD, 12
        ));

        label.setForeground(UITheme.TEXT);

        panel.add(label, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);

        return panel;
    }

    private void styleInput(JTextField field) {

        field.setFont(new Font(
                "Segoe UI", Font.PLAIN, 13
        ));

        field.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    // ================= PRODUCT TABLE =================

    private JScrollPane createProductTable() {

        String[] columns = {
                "ID", "Name", "Category",
                "Price", "Stock", "Description"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);

        UITheme.styleTable(productTable);

        productTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        productTable.setAutoCreateRowSorter(true);

        productTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int selectedRow = productTable.getSelectedRow();

                if (selectedRow != -1) {

                    int row = productTable.convertRowIndexToModel(
                            selectedRow
                    );

                    nameField.setText(
                            tableModel.getValueAt(row, 1).toString()
                    );

                    categoryField.setText(
                            tableModel.getValueAt(row, 2).toString()
                    );

                    priceField.setText(
                            tableModel.getValueAt(row, 3).toString()
                    );

                    stockField.setText(
                            tableModel.getValueAt(row, 4).toString()
                    );

                    descriptionArea.setText(
                            String.valueOf(
                                    tableModel.getValueAt(row, 5)
                            )
                    );
                }
            }
        });

        JScrollPane scroll = new JScrollPane(productTable);

        scroll.setBorder(new LineBorder(
                UITheme.BORDER, 1, true
        ));

        scroll.getViewport().setBackground(UITheme.WHITE);

        scroll.setPreferredSize(new Dimension(900, 220));

        JPanel wrapper = createSectionPanel();

        wrapper.setLayout(new BorderLayout(10, 12));

        wrapper.add(
                createSectionTitle("All Products"),
                BorderLayout.NORTH
        );

        wrapper.add(scroll, BorderLayout.CENTER);

        JScrollPane outer = new JScrollPane(wrapper);

        outer.setBorder(BorderFactory.createEmptyBorder());

        outer.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        outer.setPreferredSize(new Dimension(1000, 270));

        return outer;
    }

    // ================= RECENT ORDERS =================

    private JPanel createRecentOrders() {

        String[] columns = {
                "Order ID", "Customer", "Amount", "Date", "Status"
        };

        orderModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = new JTable(orderModel);

        UITheme.styleTable(orderTable);

        orderTable.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(orderTable);

        scroll.setBorder(new LineBorder(
                UITheme.BORDER, 1, true
        ));

        scroll.getViewport().setBackground(UITheme.WHITE);

        scroll.setPreferredSize(new Dimension(900, 150));

        JPanel wrapper = createSectionPanel();

        wrapper.setLayout(new BorderLayout(10, 12));

        wrapper.add(
                createSectionTitle("Recent Orders"),
                BorderLayout.NORTH
        );

        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    // ================= SECTION HELPERS =================

    private JPanel createSectionPanel() {

        JPanel panel = new JPanel();

        panel.setBackground(UITheme.WHITE);

        panel.setBorder(new CompoundBorder(
                new LineBorder(UITheme.BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));

        return panel;
    }

    private JLabel createSectionTitle(String text) {

        JLabel label = new JLabel(text);

        label.setFont(new Font(
                "Segoe UI", Font.BOLD, 17
        ));

        label.setForeground(UITheme.PRIMARY_DARK);

        return label;
    }

    // ================= ADD PRODUCT =================

    private void addProduct() {

        try {

            String name = nameField.getText().trim();

            String category = categoryField.getText().trim();

            String description = descriptionArea.getText().trim();

            if (name.isEmpty() || category.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product name and category are required."
                );

                return;
            }

            double price = Double.parseDouble(
                    priceField.getText().trim()
            );

            int stock = Integer.parseInt(
                    stockField.getText().trim()
            );

            if (price <= 0 || stock < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Price must be positive and stock cannot be negative."
                );

                return;
            }

            if (productDAO.addProduct(
                    name, category, price, stock, description
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                refreshAll();

            } else {

                showError();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid price and stock."
            );
        }
    }

    // ================= UPDATE PRODUCT =================

    private void updateProduct() {

        int selectedRow = productTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a product first."
            );

            return;
        }

        int row = productTable.convertRowIndexToModel(selectedRow);

        try {

            int id = Integer.parseInt(
                    tableModel.getValueAt(row, 0).toString()
            );

            String name = nameField.getText().trim();

            String category = categoryField.getText().trim();

            String description = descriptionArea.getText().trim();

            if (name.isEmpty() || category.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product name and category are required."
                );

                return;
            }

            double price = Double.parseDouble(
                    priceField.getText().trim()
            );

            int stock = Integer.parseInt(
                    stockField.getText().trim()
            );

            if (price <= 0 || stock < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter valid price and stock."
                );

                return;
            }

            if (productDAO.updateProduct(
                    id, name, category, price, stock, description
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                refreshAll();

            } else {

                showError();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid price and stock."
            );
        }
    }

    // ================= DELETE PRODUCT =================

    private void deleteProduct() {

        int selectedRow = productTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a product first."
            );

            return;
        }

        int row = productTable.convertRowIndexToModel(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this product?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            int id = Integer.parseInt(
                    tableModel.getValueAt(row, 0).toString()
            );

            if (productDAO.deleteProduct(id)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearFields();
                refreshAll();

            } else {

                showError();
            }
        }
    }

    // ================= LOAD PRODUCTS =================

    private void loadProducts() {

        tableModel.setRowCount(0);

        List<Object[]> products = productDAO.getAllProducts();

        for (Object[] product : products) {
            tableModel.addRow(product);
        }
    }

    // ================= CLEAR FIELDS =================

    private void clearFields() {

        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        stockField.setText("");
        descriptionArea.setText("");

        productTable.clearSelection();
    }

    // ================= REFRESH =================

    private void refreshAll() {

        loadProducts();
        loadAnalytics();
        loadRecentOrders();
    }

    private void loadRecentOrders() {

        orderModel.setRowCount(0);

        List<Object[]> orders = dashboardDAO.getRecentOrders();

        for (Object[] order : orders) {
            orderModel.addRow(order);
        }
    }

    // ================= ERROR =================

    private void showError() {

        JOptionPane.showMessageDialog(
                this,
                "Operation failed. Check database connection.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            UITheme.apply();

            new AdminDashboard().setVisible(true);
        });
    }
}