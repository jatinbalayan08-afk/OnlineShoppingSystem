
package dao;

import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Place an order and save its items in the database.
    public int placeOrder(
            String username,
            List<OrderItem> items,
            double totalAmount) {

        String orderSQL =
                "INSERT INTO orders (username, total_amount, status) " +
                "VALUES (?, ?, 'PLACED')";

        String itemSQL =
                "INSERT INTO order_items " +
                "(order_id, product_name, price, quantity, subtotal) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int orderId;

                try (PreparedStatement ps = con.prepareStatement(
                        orderSQL, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, username);
                    ps.setDouble(2, totalAmount);
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Order ID could not be generated.");
                        }

                        orderId = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps =
                             con.prepareStatement(itemSQL)) {

                    for (OrderItem item : items) {
                        ps.setInt(1, orderId);
                        ps.setString(2, item.productName);
                        ps.setDouble(3, item.price);
                        ps.setInt(4, item.quantity);
                        ps.setDouble(5, item.subtotal);
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                con.commit();
                return orderId;

            } catch (SQLException e) {
                con.rollback();
                throw e;

            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Fetch order history for the logged-in customer.
    public List<Object[]> getOrderHistory(String username)
            throws SQLException {

        List<Object[]> orders = new ArrayList<>();

        String sql =
                "SELECT order_id, total_amount, order_date, status " +
                "FROM orders WHERE username = ? " +
                "ORDER BY order_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Object[]{
                            rs.getInt("order_id"),
                            rs.getDouble("total_amount"),
                            rs.getTimestamp("order_date"),
                            rs.getString("status")
                    });
                }
            }
        }

        return orders;
    }

    // Fetch products belonging to a particular order.
    public List<Object[]> getOrderItems(int orderId)
            throws SQLException {

        List<Object[]> items = new ArrayList<>();

        String sql =
                "SELECT product_name, price, quantity, subtotal " +
                "FROM order_items WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new Object[]{
                            rs.getString("product_name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getDouble("subtotal")
                    });
                }
            }
        }

        return items;
    }

    // Represents an individual product in an order.
    public static class OrderItem {

        public String productName;
        public double price;
        public int quantity;
        public double subtotal;

        public OrderItem(
                String productName,
                double price,
                int quantity,
                double subtotal) {

            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
    }
}
