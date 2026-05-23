package com.asri.prototype;

import java.sql.*;

public class UserDAO {
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE LOWER(email) = LOWER(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void register(User user, String password, String securityAnswer) throws SQLException {
        String passwordSalt = PasswordUtil.generateSalt();
        String passwordHash = PasswordUtil.hash(password, passwordSalt);

        String answerHash = PasswordUtil.hash(securityAnswer.toLowerCase().trim(), passwordSalt);

        String sql = """
                INSERT INTO users
                (full_name, email, phone, date_of_birth, password_hash, salt, role, gender,
                 address, security_question, security_answer_hash)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getDateOfBirth());
            ps.setString(5, passwordHash);
            ps.setString(6, passwordSalt);
            ps.setString(7, user.getRole());
            ps.setString(8, user.getGender());
            ps.setString(9, user.getAddress());
            ps.setString(10, user.getSecurityQuestion());
            ps.setString(11, answerHash);
            ps.executeUpdate();
        }
    }

    public User login(String email, String password) throws SQLException {
        String sql = """
                SELECT id, full_name, email, phone, date_of_birth, password_hash, salt,
                       role, gender, address, security_question
                FROM users
                WHERE LOWER(email) = LOWER(?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            String storedHash = rs.getString("password_hash");
            String salt = rs.getString("salt");

            if (!PasswordUtil.verify(password, storedHash, salt)) {
                return null;
            }

            return new User(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("date_of_birth"),
                    rs.getString("role"),
                    rs.getString("gender"),
                    rs.getString("address"),
                    rs.getString("security_question")
            );
        }
    }
}
