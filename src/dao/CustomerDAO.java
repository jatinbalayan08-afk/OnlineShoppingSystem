package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomerDAO {

public boolean registerCustomer(
        String name,
        String username,
        String email,
        String password) throws SQLException {

    String sql = "INSERT INTO customers "
            + "(full_name, username, email, password_hash) "
            + "VALUES (?, ?, ?, ?)";

    String passwordHash = hashPassword(password);

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, name);
        statement.setString(2, username);
        statement.setString(3, email);
        statement.setString(4, passwordHash);

        return statement.executeUpdate() == 1;
    }
}

private String hashPassword(String password) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();

        for (byte b : hash) {
            result.append(String.format("%02x", b & 0xff));
        }

        return result.toString();

    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("Password hashing failed.", e);
    }
}
}
