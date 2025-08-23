package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tss.database.DBConnection;
import com.tss.dto.UserFdView;
import com.tss.model.FdApplication;
import com.tss.model.FixedDeposit;

public class FdDAO {

    // Apply for FD
    public void createFdApplication(FdApplication app) {
        String sql = "INSERT INTO fd_applications (user_id, account_id, amount, tenure_months, interest_rate, status) VALUES (?, ?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, app.getUserId());
            ps.setInt(2, app.getAccountId());
            ps.setDouble(3, app.getAmount());
            ps.setInt(4, app.getTenureMonths());
            ps.setDouble(5, app.getInterestRate());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    app.setFdAppId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all pending FD applications
    public List<FdApplication> getPendingApplications() {
        List<FdApplication> apps = new ArrayList<>();
        String sql = "SELECT * FROM fd_applications WHERE status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                apps.add(mapToApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    // Get FD applications by user
    public List<FdApplication> getApplicationsByUser(int userId) {
        List<FdApplication> apps = new ArrayList<>();
        String sql = "SELECT * FROM fd_applications WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    apps.add(mapToApplication(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    // Approve FD: update status and create FD
    public boolean approveFd(int fdAppId, int adminId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Get application
            FdApplication app = getApplicationById(fdAppId, conn);
            if (app == null || !"PENDING".equals(app.getStatus())) {
                return false;
            }

            // Update application
            String updateSql = "UPDATE fd_applications SET status = 'APPROVED', approved_by = ?, approved_at = NOW() WHERE fd_app_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, adminId);
                ps.setInt(2, fdAppId);
                ps.executeUpdate();
            }

            // Insert into fixed_deposits
            double maturityAmount = app.getAmount() * Math.pow(1 + app.getInterestRate() / 100 / 12, app.getTenureMonths());
            maturityAmount = Math.round(maturityAmount * 100.0) / 100.0;

            String insertFdSql = "INSERT INTO fixed_deposits (fd_app_id, user_id, account_id, amount, tenure_months, interest_rate, maturity_amount, start_date, maturity_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? MONTH), 'ACTIVE')";
            try (PreparedStatement ps = conn.prepareStatement(insertFdSql)) {
                ps.setInt(1, fdAppId);
                ps.setInt(2, app.getUserId());
                ps.setInt(3, app.getAccountId());
                ps.setDouble(4, app.getAmount());
                ps.setInt(5, app.getTenureMonths());
                ps.setDouble(6, app.getInterestRate());
                ps.setDouble(7, maturityAmount);
                ps.setInt(8, app.getTenureMonths());
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { }
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { }
        }
    }

    // Reject FD
    public boolean rejectFd(int fdAppId, String reason) {
        String sql = "UPDATE fd_applications SET status = 'REJECTED', rejection_reason = ? WHERE fd_app_id = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reason);
            ps.setInt(2, fdAppId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get single application
    public FdApplication getApplicationById(int fdAppId) {
        String sql = "SELECT * FROM fd_applications WHERE fd_app_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fdAppId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToApplication(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FdApplication getApplicationById(int fdAppId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM fd_applications WHERE fd_app_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fdAppId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToApplication(rs);
                }
            }
        }
        return null;
    }

    private FdApplication mapToApplication(ResultSet rs) throws SQLException {
        FdApplication app = new FdApplication();
        app.setFdAppId(rs.getInt("fd_app_id"));
        app.setUserId(rs.getInt("user_id"));
        app.setAccountId(rs.getInt("account_id"));
        app.setAmount(rs.getDouble("amount"));
        app.setTenureMonths(rs.getInt("tenure_months"));
        app.setInterestRate(rs.getDouble("interest_rate"));
        app.setApplicationDate(rs.getTimestamp("application_date").toLocalDateTime());
        app.setStatus(rs.getString("status"));
        app.setRejectionReason(rs.getString("rejection_reason"));
        app.setApprovedBy(rs.getObject("approved_by", Integer.class));
        app.setApprovedAt(rs.getTimestamp("approved_at") != null ? rs.getTimestamp("approved_at").toLocalDateTime() : null);
        return app;
    }

    public List<UserFdView> getUserFdSummary(int userId) {
        List<UserFdView> summary = new ArrayList<>();

        // 1. Applications (PENDING, REJECTED, APPROVED but not yet in FD table)
        String appSql = "SELECT fd_app_id, amount, tenure_months, interest_rate, application_date, status, rejection_reason " +
                        "FROM fd_applications WHERE user_id = ? ORDER BY application_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(appSql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    summary.add(new UserFdView(
                        rs.getInt("fd_app_id"),
                        rs.getDouble("amount"),
                        rs.getInt("tenure_months"),
                        rs.getDouble("interest_rate"),
                        rs.getTimestamp("application_date").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getString("rejection_reason")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Fixed Deposits (ACTIVE, MATURED, CLOSED)
        String fdSql = "SELECT fd_id, fd_app_id, amount, tenure_months, interest_rate, start_date, maturity_date, maturity_amount, status " +
                       "FROM fixed_deposits WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(fdSql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    summary.add(new UserFdView(
                        rs.getInt("fd_id"),
                        rs.getInt("fd_app_id"),
                        rs.getDouble("amount"),
                        rs.getInt("tenure_months"),
                        rs.getDouble("interest_rate"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("maturity_date").toLocalDate(),
                        rs.getDouble("maturity_amount"),
                        rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return summary;
    }

    
    public boolean closeFixedDeposit(int fdId) {
        String sql = "UPDATE fixed_deposits SET status = 'CLOSED', updated_at = CURRENT_TIMESTAMP WHERE fd_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, fdId);
            
            int rowsAffected = ps.executeUpdate();
            
            // Returns true if one active FD was closed
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateStatusFixedDeposit(int fdId) {
        String sql = "UPDATE fd_applications SET status = 'CLOSED' WHERE fd_app_id = ? AND AND status = 'APPROVED'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, fdId);
            
            int rowsAffected = ps.executeUpdate();
            
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<FixedDeposit> getActiveFixedDeposits() {
        List<FixedDeposit> fds = new ArrayList<>();
        String sql = "SELECT * FROM fixed_deposits WHERE status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                fds.add(mapToFD(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fds;
    }
    
 // Add this method to your existing FdDAO.java
    public List<FixedDeposit> getAllFixedDeposits() {
        List<FixedDeposit> fds = new ArrayList<>();
        String sql = "SELECT * FROM fixed_deposits ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                fds.add(mapToFD(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fds;
    }
    
    public FixedDeposit getFixedDepositById(int fdId) {
        String sql = "SELECT * FROM fixed_deposits WHERE fd_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fdId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapToFD(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private FixedDeposit mapToFD(ResultSet rs) throws SQLException {
        FixedDeposit fd = new FixedDeposit();
        fd.setFdId(rs.getInt("fd_id"));
        fd.setFdAppId(rs.getInt("fd_app_id"));
        fd.setUserId(rs.getInt("user_id"));
        fd.setAccountId(rs.getInt("account_id"));
        fd.setAmount(rs.getDouble("amount"));
        fd.setTenureMonths(rs.getInt("tenure_months"));
        fd.setInterestRate(rs.getDouble("interest_rate"));
        fd.setMaturityAmount(rs.getDouble("maturity_amount"));
        fd.setStartDate(rs.getDate("start_date").toLocalDate());
        fd.setMaturityDate(rs.getDate("maturity_date").toLocalDate());
        fd.setStatus(rs.getString("status"));
        fd.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        fd.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return fd;
    }
}