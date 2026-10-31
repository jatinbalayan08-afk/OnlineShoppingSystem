package gui;

import dao.OrderDAO;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.util.List;

public class OrderHistoryFrame extends JFrame {

    private final String username;
    private final DefaultTableModel model;
    private final JTable orderTable;

    public OrderHistoryFrame(String username) {

        this.username = username;

        setTitle("ShopEase | Order History");
        setSize(1050, 680);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);

        String[] columns = {
            "Order ID", "Total Amount (INR)", "Order Date", "Status"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = new JTable(model);

        UITheme.styleTable(orderTable);

        orderTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        orderTable.setAutoCreateRowSorter(true);

        add(createMainContent(), BorderLayout.CENTER);

        add(createBottomPanel(), BorderLayout.SOUTH);

        loadOrders();
    }

    // ================= HEADER =================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(UITheme.WHITE);

        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UITheme.BORDER),
            new EmptyBorder(18, 30, 18, 30)
        ));

        JPanel brandPanel = new JPanel();

        brandPanel.setOpaque(false);

        brandPanel.setLayout(new BoxLayout(
            brandPanel, BoxLayout.Y_AXIS
        ));

        JLabel brand = new JLabel("ShopEase");

        brand.setFont(new Font(
            "Segoe UI", Font.BOLD, 26
        ));

        brand.setForeground(UITheme.PRIMARY_DARK);

        JLabel subtitle = new JLabel("YOUR ORDER HISTORY");

        subtitle.setFont(new Font(
            "Segoe UI", Font.PLAIN, 10
        ));

        subtitle.setForeground(UITheme.MUTED);

        brandPanel.add(brand);
        brandPanel.add(subtitle);

        JLabel welcome = new JLabel("Welcome, " + username);

        welcome.setFont(new Font(
            "Segoe UI", Font.BOLD, 13
        ));

        welcome.setForeground(UITheme.TEXT);

        header.add(brandPanel, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        return header;
    }

    // ================= MAIN CONTENT =================

    private JPanel createMainContent() {

        JPanel main = new JPanel(new BorderLayout(15, 20));

        main.setBackground(UITheme.BACKGROUND);

        main.setBorder(new EmptyBorder(28, 30, 20, 30));

        // Heading

        JPanel headingPanel = new JPanel();

        headingPanel.setOpaque(false);

        headingPanel.setLayout(new BoxLayout(
            headingPanel, BoxLayout.Y_AXIS
        ));

        JLabel heading = UITheme.createTitle("My Orders");

        JLabel description = new JLabel(
            "Track your purchases and view your order information."
        );

        description.setFont(new Font(
            "Segoe UI", Font.PLAIN, 14
        ));

        description.setForeground(UITheme.MUTED);

        headingPanel.add(heading);

        headingPanel.add(Box.createVerticalStrut(7));

        headingPanel.add(description);

        // Table

        JScrollPane scrollPane = new JScrollPane(orderTable);

        scrollPane.setBorder(new LineBorder(
            UITheme.BORDER, 1, true
        ));

        scrollPane.getViewport().setBackground(UITheme.WHITE);

        scrollPane.getVerticalScrollBar().setUnitIncrement(15);

        JPanel tableCard = new JPanel(new BorderLayout());

        tableCard.setBackground(UITheme.WHITE);

        tableCard.setBorder(new EmptyBorder(12, 12, 12, 12));

        tableCard.add(scrollPane, BorderLayout.CENTER);

        main.add(headingPanel, BorderLayout.NORTH);

        main.add(tableCard, BorderLayout.CENTER);

        return main;
    }

    // ================= BOTTOM BUTTONS =================

    private JPanel createBottomPanel() {

        JPanel bottom = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 15)
        );

        bottom.setBackground(UITheme.WHITE);

        bottom.setBorder(new MatteBorder(
            1, 0, 0, 0, UITheme.BORDER
        ));

        JButton refreshButton =
            UITheme.createButton("Refresh");

        JButton detailsButton =
            UITheme.createButton("View Order Details");

        JButton invoiceButton =
            UITheme.createButton("Download Invoice (PDF)");

        JButton closeButton =
            UITheme.secondaryButton("Close");

        refreshButton.addActionListener(e -> loadOrders());

        detailsButton.addActionListener(e -> showOrderDetails());

        invoiceButton.addActionListener(e -> generateInvoice());

        closeButton.addActionListener(e -> dispose());

        bottom.add(refreshButton);
        bottom.add(detailsButton);
        bottom.add(invoiceButton);
        bottom.add(closeButton);

        return bottom;
    }

    // ================= LOAD ORDERS =================

    private void loadOrders() {

        model.setRowCount(0);

        try {

            OrderDAO dao = new OrderDAO();

            List<Object[]> orders =
                dao.getOrderHistory(username);

            orders.sort((a, b) ->
    Integer.compare(
        Integer.parseInt(a[0].toString()),
        Integer.parseInt(b[0].toString())
    )
);

for (Object[] order : orders) {
    model.addRow(order);
}

            if (orders.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "No orders found.",
                    "Order History",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Unable to load orders: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= SELECT ORDER =================

    private int getSelectedOrder() {

        int selectedRow = orderTable.getSelectedRow();

        if (selectedRow < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Please select an order first.",
                "No Order Selected",
                JOptionPane.WARNING_MESSAGE
            );

            return -1;
        }

        return orderTable.convertRowIndexToModel(selectedRow);
    }

    // ================= ORDER DETAILS =================

    private void showOrderDetails() {

        int row = getSelectedOrder();

        if (row < 0) {
            return;
        }

        int orderId = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        try {

            OrderDAO dao = new OrderDAO();

            List<Object[]> items = dao.getOrderItems(orderId);

            String[] columns = {
                "Product", "Price (INR)", "Quantity", "Subtotal (INR)"
            };

            DefaultTableModel detailsModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                        int row, int column
                    ) {
                        return false;
                    }
                };

            for (Object[] item : items) {
                detailsModel.addRow(item);
            }

            JTable detailsTable = new JTable(detailsModel);

            UITheme.styleTable(detailsTable);

            JScrollPane scrollPane =
                new JScrollPane(detailsTable);

            scrollPane.setPreferredSize(
                new Dimension(600, 280)
            );

            JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Order Details - #" + orderId,
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Unable to load order details: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= PDF INVOICE =================

    private void generateInvoice() {

        int row = getSelectedOrder();

        if (row < 0) {
            return;
        }

        int orderId = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        Object total = model.getValueAt(row, 1);
        Object date = model.getValueAt(row, 2);
        Object status = model.getValueAt(row, 3);

        try {

            OrderDAO dao = new OrderDAO();

            List<Object[]> items = dao.getOrderItems(orderId);

            StringBuilder html = new StringBuilder();

            html.append("""
                <html>
                <body style='font-family:Arial; padding:25px;'>
                <h1 style='text-align:center; color:#1F4E79;'>
                    ShopEase
                </h1>
                <h2 style='text-align:center;'>ORDER INVOICE</h2>
                <hr>
                """);

            html.append("<h3>Order Information</h3>");

            html.append("<p><b>Order ID:</b> #")
                .append(orderId).append("</p>");

            html.append("<p><b>Customer:</b> ")
                .append(username).append("</p>");

            html.append("<p><b>Order Date:</b> ")
                .append(date).append("</p>");

            html.append("<p><b>Status:</b> ")
                .append(status).append("</p>");

            html.append("""
                <br>
                <table border='1' cellpadding='8'
                       cellspacing='0' width='100%'>
                <tr style='background-color:#EAF1F8;'>
                <th>Product</th>
                <th>Price (INR)</th>
                <th>Quantity</th>
                <th>Subtotal (INR)</th>
                </tr>
                """);

            for (Object[] item : items) {

                html.append("<tr>");

                for (Object value : item) {

                    html.append("<td>")
                        .append(value)
                        .append("</td>");
                }

                html.append("</tr>");
            }

            html.append("</table><br><hr>");

            html.append(
                "<h2 style='text-align:right; color:#1F4E79;'>"
            );

            html.append("Total Amount: INR ")
                .append(total)
                .append("</h2>");

            html.append("""
                <br>
                <p style='text-align:center;'>
                    Thank you for shopping with ShopEase!
                </p>
                </body>
                </html>
                """);

            JEditorPane invoice = new JEditorPane();

            invoice.setContentType("text/html");

            invoice.setText(html.toString());

            invoice.setEditable(false);

            invoice.setSize(600, 900);

            boolean printed = invoice.print(
                null,
                null,
                true,
                null,
                null,
                true
            );

            if (printed) {

                JOptionPane.showMessageDialog(
                    this,
                    "Invoice printing completed!\n"
                    + "Select Microsoft Print to PDF to save your invoice.",
                    "Invoice",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Unable to generate invoice: " + e.getMessage(),
                "Invoice Error",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (PrinterException e) {

            JOptionPane.showMessageDialog(
                this,
                "Printing failed: " + e.getMessage(),
                "Printing Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}