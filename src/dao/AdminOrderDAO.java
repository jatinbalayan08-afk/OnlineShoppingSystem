package dao;

import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderDAO {

    // Get all orders or search by customer username
    public List<Object[]> getAllOrders(String search) {

        List<Object[]> orders = new ArrayList<>();

        String sql = "SELECT order_id, username, total_amount, "
                   + "order_date, status FROM orders "
                   + "WHERE username LIKE ? "
                   + "ORDER BY order_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + search.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    orders.add(new Object[]{
                        rs.getInt("order_id"),
                        rs.getString("username"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("order_date"),
                        rs.getString("status")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, String status) {

        String[] allowedStatuses = {
            "PLACED", "SHIPPED", "DELIVERED", "CANCELLED"
        };

        boolean valid = false;

        for (String allowed : allowedStatuses) {
            if (allowed.equals(status)) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            return false;
        }

        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get order details
    public List<Object[]> getOrderDetails(int orderId) {

        List<Object[]> items = new ArrayList<>();

        String sql = "SELECT product_name, price, quantity, subtotal "
                   + "FROM order_items WHERE order_id = ?";

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}