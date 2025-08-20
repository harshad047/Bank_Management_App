// src/main/java/com/tss/dao/PassbookDAO.java
package com.tss.dao;

import com.tss.database.DBConnection;
import com.tss.model.Account;
import com.tss.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PassbookDAO {

    public Account getAccountByUserId(int userId) {
        String sql = "SELECT u.user_id, u.username, u.account_type, u.status, a.account_id, a.account_number, a.balance " +
                     "FROM users u JOIN accounts a ON u.user_id = a.user_id WHERE u.user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account acc = new Account();
                    acc.setUserId(rs.getInt("user_id"));
                    acc.setAccountHolderName(rs.getString("username"));
                    acc.setAccountType(rs.getString("account_type"));
                    acc.setStatus(rs.getString("status"));
                    acc.setAccountId(rs.getInt("account_id"));
                    acc.setAccountNumber(rs.getString("account_number"));
                    acc.setBalance(rs.getBigDecimal("balance"));
                    return acc;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Transaction> getRecentTransactions(int userId, int limit) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id=? ORDER BY txn_time DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction txn = new Transaction();
                    txn.setTxnId(rs.getInt("txn_id"));
                    txn.setAccountId(rs.getInt("account_id"));
                    txn.setUserId(rs.getInt("user_id"));
                    txn.setTxnType(rs.getString("txn_type"));
                    txn.setAmount(rs.getBigDecimal("amount"));
                    txn.setDescription(rs.getString("description"));
                    txn.setTxnTime(rs.getTimestamp("txn_time"));
                    txn.setBalanceAfter(rs.getBigDecimal("balance_after"));
                    txn.setChannel(rs.getString("channel"));
                    list.add(txn);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
