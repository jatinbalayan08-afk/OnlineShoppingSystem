package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class LoginDAO {

public boolean authenticate(String username, String password)
        throws SQLException {

    String sql = "SELECT password_hash FROM customers WHERE username = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, username);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String enteredHash = hashPassword(password);

                return MessageDigest.isEqual(
                    storedHash.getBytes(StandardCharsets.UTF_8),
                    enteredHash.getBytes(StandardCharsets.UTF_8)
                );
            }

            return false;
        }
    }
}

private String hashPassword(String password) {

    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();

        for (byte b : hash) {
            hex.append(String.format("%02x", b & 0xff));
        }

        return hex.toString();

    } catch (Exception e) {
        throw new IllegalStateException("Password verification failed.", e);
    }
}

}