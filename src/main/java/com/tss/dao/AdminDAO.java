package com.tss.dao;

import com.tss.model.Admin;
import com.tss.database.DBConnection;
import java.sql.*;

public class AdminDAO {

    // Find admin by username
    public Admin findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setAdminId(rs.getInt("admin_id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setPassword(rs.getString("password"));
                    admin.setEmail(rs.getString("email"));
                    admin.setSuperAdmin(rs.getBoolean("is_super_admin"));
                    admin.setCreatedAt(rs.getTimestamp("created_at"));
                    return admin;
                }
            }
        }
        return null;
    }

    // Insert new admin
    public boolean createAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO admins (username, password, email, is_super_admin, created_at) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, admin.getUsername());
                ps.setString(2, admin.getPassword());
                ps.setString(3, admin.getEmail());
                ps.setBoolean(4, admin.isSuperAdmin());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Check if username or email already exists
    public boolean existsByUsernameOrEmail(String username, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admins WHERE username = ? OR email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}