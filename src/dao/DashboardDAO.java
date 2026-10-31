package dao;

import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    public int getTotalProducts() {
        return getCount("SELECT COUNT(*) FROM products");
    }

    public int getTotalCustomers() {
        return getCount("SELECT COUNT(*) FROM customers");
    }

    public int getTotalOrders() {
        return getCount("SELECT COUNT(*) FROM orders");
    }

    private int getCount(String sql) {
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

    public double getTotalSales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Object[]> getRecentOrders() {

        List<Object[]> orders = new ArrayList<>();

        String sql = "SELECT order_id, username, total_amount, "
                   + "order_date, status FROM orders "
                   + "ORDER BY order_date DESC LIMIT 10";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(new Object[]{
                    rs.getInt("order_id"),
                    rs.getString("username"),
                    rs.getDouble("total_amount"),
                    rs.getTimestamp("order_date"),
                    rs.getString("status")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}