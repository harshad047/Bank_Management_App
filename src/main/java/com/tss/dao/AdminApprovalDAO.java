package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tss.database.DBConnection;
import com.tss.model.AdminApproval;

public class AdminApprovalDAO {

    public boolean logApproval(int userId, int adminId, String action, String reason) throws SQLException {
        String sql = "INSERT INTO admin_approvals (user_id, admin_id, action, reason) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, adminId);
                ps.setString(3, action);
                ps.setString(4, reason);
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
    public List<AdminApproval> getTodayApprovals() throws SQLException {
        List<AdminApproval> approvals = new ArrayList<>();
        String sql = "SELECT * FROM admin_approvals WHERE DATE(created_at) = CURDATE() ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AdminApproval a = new AdminApproval();
                a.setApprovalId(rs.getInt("approval_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setAdminId(rs.getInt("admin_id"));
                a.setAction(rs.getString("action"));
                a.setReason(rs.getString("reason"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                approvals.add(a);
            }
        }
        return approvals;
    }
    
 // inside AdminApprovalDao.java
    public List<AdminApproval> getRecentApprovals(int limit) throws SQLException {
        List<AdminApproval> approvals = new ArrayList<>();
        String sql = "SELECT * FROM admin_approvals ORDER BY created_at DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdminApproval a = new AdminApproval();
                    a.setApprovalId(rs.getInt("approval_id"));
                    a.setUserId(rs.getInt("user_id"));
                    a.setAdminId(rs.getInt("admin_id"));
                    a.setAction(rs.getString("action"));
                    a.setReason(rs.getString("reason"));
                    a.setCreatedAt(rs.getTimestamp("created_at"));
                    approvals.add(a);
                }
            }
        }
        return approvals;
    }

}