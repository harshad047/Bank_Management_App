package com.tss.dao;

import com.tss.model.Account;
import com.tss.database.DBConnection;
import java.math.BigDecimal;
import java.sql.*;

public class AccountDAO {

    // Generate a unique 12-digit account number
    public String generateAccountNumber() throws SQLException {
        String accNum;
        do {
            accNum = "AC" + String.format("%010d", (int)(Math.random() * 1000000000));
        } while (existsByAccountNumber(accNum));
        return accNum;
    }

    // Check if account number already exists
    public boolean existsByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Create new account
    public boolean createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (user_id, account_number, created_at) VALUES (?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, account.getUserId());
                ps.setString(2, account.getAccountNumber());
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

    // Get account by user ID
   
    public Account findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToAccount(rs);
                }
            }
        }
        return null;
    }
    
    public boolean updateBalance(int accountId, BigDecimal balance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ?, updated_at = NOW() WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setBigDecimal(1, balance);
                ps.setInt(2, accountId);
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
    
    public BigDecimal getTotalBalance() throws SQLException {
        String sql = "SELECT COALESCE(SUM(balance), 0) FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        }
    }
    private Account mapToAccount(ResultSet rs) throws SQLException {
        Account acc = new Account();
        acc.setAccountId(rs.getInt("account_id"));
        acc.setUserId(rs.getInt("user_id"));
        acc.setAccountNumber(rs.getString("account_number"));
        acc.setBalance(rs.getBigDecimal("balance"));
        acc.setCreatedAt(rs.getTimestamp("created_at"));
        return acc;
    }
    
    public Integer getAccountIdByUserId(int userId) {
        // Example: query accounts table
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("account_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}