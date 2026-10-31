package dao;

import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminCustomerDAO {

    // Get all customers or search customers
    public List<Object[]> getAllCustomers(String search) {

        List<Object[]> customers = new ArrayList<>();

        String sql = "SELECT full_name, username, email "
                   + "FROM customers "
                   + "WHERE full_name LIKE ? "
                   + "OR username LIKE ? "
                   + "OR email LIKE ? "
                   + "ORDER BY username";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String keyword = "%" + search.trim() + "%";

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    customers.add(new Object[]{
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    // Get customer order history
    public List<Object[]> getCustomerOrders(String username) {

        List<Object[]> orders = new ArrayList<>();

        String sql = "SELECT order_id, total_amount, "
                   + "order_date, status "
                   + "FROM orders WHERE username = ? "
                   + "ORDER BY order_date DESC";

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // Get total customer count
    public int getTotalCustomers() {

        String sql = "SELECT COUNT(*) FROM customers";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}