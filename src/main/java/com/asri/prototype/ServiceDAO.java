package com.asri.prototype;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    public void add(ServiceOrder order) throws SQLException {
        String sql = """
                INSERT INTO service_orders
                (customer_id, service_name, price, status, priority, start_date, end_date, description)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, order.getCustomerId());
            ps.setString(2, order.getServiceName());
            ps.setDouble(3, order.getPrice());
            ps.setString(4, order.getStatus());
            ps.setString(5, order.getPriority());
            ps.setString(6, order.getStartDate());
            ps.setString(7, order.getEndDate());
            ps.setString(8, order.getDescription());
            ps.executeUpdate();
        }
    }

    public void update(ServiceOrder order) throws SQLException {
        String sql = """
                UPDATE service_orders
                SET customer_id = ?, service_name = ?, price = ?, status = ?, priority = ?,
                    start_date = ?, end_date = ?, description = ?
                WHERE id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, order.getCustomerId());
            ps.setString(2, order.getServiceName());
            ps.setDouble(3, order.getPrice());
            ps.setString(4, order.getStatus());
            ps.setString(5, order.getPriority());
            ps.setString(6, order.getStartDate());
            ps.setString(7, order.getEndDate());
            ps.setString(8, order.getDescription());
            ps.setInt(9, order.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM service_orders WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<ServiceOrder> getAll() throws SQLException {
        String sql = """
                SELECT s.*, c.name AS customer_name
                FROM service_orders s
                JOIN customers c ON c.id = s.customer_id
                ORDER BY s.id DESC
                """;

        return queryOrders(sql, null);
    }

    public List<ServiceOrder> search(String keyword) throws SQLException {
        String sql = """
                SELECT s.*, c.name AS customer_name
                FROM service_orders s
                JOIN customers c ON c.id = s.customer_id
                WHERE CAST(s.id AS TEXT) LIKE ?
                   OR CAST(s.customer_id AS TEXT) LIKE ?
                   OR LOWER(c.name) LIKE LOWER(?)
                   OR LOWER(s.service_name) LIKE LOWER(?)
                   OR LOWER(s.status) LIKE LOWER(?)
                ORDER BY s.id DESC
                """;

        String search = "%" + keyword + "%";
        return queryOrders(sql, search);
    }

    private List<ServiceOrder> queryOrders(String sql, String keyword) throws SQLException {
        List<ServiceOrder> orders = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (keyword != null) {
                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ps.setString(3, keyword);
                ps.setString(4, keyword);
                ps.setString(5, keyword);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orders.add(new ServiceOrder(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("service_name"),
                        rs.getDouble("price"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("description")
                ));
            }
        }

        return orders;
    }
}
