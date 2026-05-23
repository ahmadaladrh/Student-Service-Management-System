package com.asri.prototype;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public void add(Customer customer) throws SQLException {
        String sql = """
                INSERT INTO customers (name, phone, email, address, customer_type, notes)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getCustomerType());
            ps.setString(6, customer.getNotes());
            ps.executeUpdate();
        }
    }

    public void update(Customer customer) throws SQLException {
        String sql = """
                UPDATE customers
                SET name = ?, phone = ?, email = ?, address = ?, customer_type = ?, notes = ?
                WHERE id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getCustomerType());
            ps.setString(6, customer.getNotes());
            ps.setInt(7, customer.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean exists(int id) throws SQLException {
        String sql = "SELECT id FROM customers WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM customers ORDER BY id DESC";
        return queryCustomers(sql, null);
    }

    public List<Customer> search(String keyword) throws SQLException {
        String sql = """
                SELECT * FROM customers
                WHERE CAST(id AS TEXT) LIKE ?
                   OR LOWER(name) LIKE LOWER(?)
                   OR phone LIKE ?
                   OR LOWER(email) LIKE LOWER(?)
                ORDER BY id DESC
                """;

        String search = "%" + keyword + "%";
        return queryCustomers(sql, search);
    }

    private List<Customer> queryCustomers(String sql, String keyword) throws SQLException {
        List<Customer> customers = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (keyword != null) {
                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ps.setString(3, keyword);
                ps.setString(4, keyword);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("customer_type"),
                        rs.getString("notes")
                ));
            }
        }

        return customers;
    }
}
