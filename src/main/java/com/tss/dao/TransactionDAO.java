package com.tss.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.tss.database.DBConnection;
import com.tss.model.Account;
import com.tss.model.MonthlySummary;
import com.tss.model.Transaction;

public class TransactionDAO {

    public boolean insertTransaction(Transaction txn) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, user_id, txn_type, amount, description, txn_time, balance_after, channel) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, txn.getAccountId());
                ps.setInt(2, txn.getUserId());
                ps.setString(3, txn.getTxnType());
                ps.setBigDecimal(4, txn.getAmount());
                ps.setString(5, txn.getDescription());
                ps.setTimestamp(6, new Timestamp(txn.getTxnTime().getTime()));
                ps.setBigDecimal(7, txn.getBalanceAfter());
                ps.setString(8, txn.getChannel());
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

    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY txn_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction();
                    t.setTxnId(rs.getInt("txn_id"));
                    t.setAccountId(rs.getInt("account_id"));
                    t.setUserId(rs.getInt("user_id"));
                    t.setTxnType(rs.getString("txn_type"));
                    t.setAmount(rs.getBigDecimal("amount"));
                    t.setDescription(rs.getString("description"));
                    t.setTxnTime(rs.getTimestamp("txn_time"));
                    t.setBalanceAfter(rs.getBigDecimal("balance_after"));
                    t.setChannel(rs.getString("channel"));
                    transactions.add(t);
                }
            }
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByDateRange(int userId, java.util.Date from, java.util.Date to) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? AND txn_time BETWEEN ? AND ? ORDER BY txn_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setTimestamp(2, new Timestamp(from.getTime()));
            ps.setTimestamp(3, new Timestamp(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction t = mapToTransaction(rs);
                    transactions.add(t);
                }
            }
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByAmountRange(int userId, BigDecimal min, BigDecimal max) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? AND amount BETWEEN ? AND ? ORDER BY txn_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setBigDecimal(2, min);
            ps.setBigDecimal(3, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY txn_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapToTransaction(rs));
            }
        }
        return transactions;
    }

//    public List<MonthlySummary> getMonthlySummary(int userId) throws SQLException {
//        String sql = """
//            SELECT 
//                YEAR(txn_time) as year,
//                MONTH(txn_time) as month,
//                SUM(CASE WHEN txn_type = 'CREDIT' THEN amount ELSE 0 END) as total_credit,
//                SUM(CASE WHEN txn_type = 'DEBIT' THEN amount ELSE 0 END) as total_debit
//            FROM transactions 
//            WHERE user_id = ?
//            GROUP BY YEAR(txn_time), MONTH(txn_time)
//            ORDER BY year, month
//            """;
//
//        List<MonthlySummary> summary = new ArrayList<>();
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, userId);
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    MonthlySummary ms = new MonthlySummary();
//                    ms.setYear(rs.getInt("year"));
//                    ms.setMonth(rs.getInt("month"));
//                    ms.setTotalCredit(rs.getBigDecimal("total_credit"));
//                    ms.setTotalDebit(rs.getBigDecimal("total_debit"));
//                    summary.add(ms);
//                }
//            }
//        }
//        return summary;
//    }

    public long getTotalTransactionCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM transactions";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }
    
 // com.tss.dao.TransactionDAO.java

    public List<Transaction> getRecentTransactions(int limit) throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY txn_time DESC LIMIT ?";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapToTransaction(rs));
                }
            }
        }
        return transactions;
    }
    
 // com.tss.dao.TransactionDAO.java

    public List<MonthlySummary> getMonthlySummaryForAllUsers() throws SQLException {
        String sql = """
            SELECT 
                YEAR(txn_time) as year,
                MONTH(txn_time) as month,
                SUM(CASE WHEN txn_type = 'CREDIT' THEN amount ELSE 0 END) as total_credit,
                SUM(CASE WHEN txn_type = 'DEBIT' THEN amount ELSE 0 END) as total_debit
            FROM transactions 
            GROUP BY YEAR(txn_time), MONTH(txn_time)
            ORDER BY year, month
            """;

        List<MonthlySummary> summary = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MonthlySummary ms = new MonthlySummary();
                ms.setYear(rs.getInt("year"));
                ms.setMonth(rs.getInt("month"));
                ms.setTotalCredit(rs.getBigDecimal("total_credit"));
                ms.setTotalDebit(rs.getBigDecimal("total_debit"));
                summary.add(ms);
            }
        }
        return summary;
    }
    
 // com.tss.dao.TransactionDAO.java
    public List<Transaction> getTransactionsWithFilters(
            BigDecimal minAmount, BigDecimal maxAmount,
            java.util.Date fromDate, java.util.Date toDate,
            String accountType) throws SQLException {

        StringBuilder sql = new StringBuilder("""
            SELECT t.* FROM transactions t
            JOIN users u ON t.user_id = u.user_id
            WHERE 1=1
            """);

        List<Object> params = new ArrayList<>();

        if (minAmount != null) {
            sql.append(" AND t.amount >= ? ");
            params.add(minAmount);
        }
        if (maxAmount != null) {
            sql.append(" AND t.amount <= ? ");
            params.add(maxAmount);
        }
        if (fromDate != null) {
            sql.append(" AND t.txn_time >= ? ");
            params.add(new Timestamp(fromDate.getTime()));
        }
        if (toDate != null) {
            sql.append(" AND t.txn_time <= ? ");
            params.add(new Timestamp(toDate.getTime()));
        }
        if (accountType != null && !accountType.isEmpty()) {
            sql.append(" AND u.account_type = ? ");
            params.add(accountType);
        }

        sql.append(" ORDER BY t.txn_time DESC");

        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    // Utility method
    private Transaction mapToTransaction(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTxnId(rs.getInt("txn_id"));
        t.setAccountId(rs.getInt("account_id"));
        t.setUserId(rs.getInt("user_id"));
        t.setTxnType(rs.getString("txn_type"));
        t.setAmount(rs.getBigDecimal("amount"));
        t.setDescription(rs.getString("description"));
        t.setTxnTime(rs.getTimestamp("txn_time"));
        t.setBalanceAfter(rs.getBigDecimal("balance_after"));
        t.setChannel(rs.getString("channel"));
        return t;
    }
    public boolean addTransaction(Transaction txn) {
        String updateBalance = txn.getTxnType().equalsIgnoreCase("CREDIT")
                ? "UPDATE accounts SET balance = balance + ? WHERE account_id=?"
                : "UPDATE accounts SET balance = balance - ? WHERE account_id=?";

        String insertTxn = "INSERT INTO transactions (user_id, account_id, txn_type, amount, description, balance_after,txn_time) VALUES (?, ?, ?, ?, ?, ?,?)";

        Connection conn = null;
        PreparedStatement psBalance = null;
        PreparedStatement psTxn = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Update balance
            psBalance = conn.prepareStatement(updateBalance);
            psBalance.setBigDecimal(1, txn.getAmount());
            psBalance.setInt(2, txn.getAccountId());
            int updated = psBalance.executeUpdate();
            if (updated == 0) {
                conn.rollback();
                return false;
            }

            // 2. Get new balance
            BigDecimal newBalance = null;
            try (PreparedStatement psBal = conn.prepareStatement("SELECT balance FROM accounts WHERE account_id=?")) {
                psBal.setInt(1, txn.getAccountId());
                rs = psBal.executeQuery();
                if (rs.next()) {
                    newBalance = rs.getBigDecimal("balance");
                }
            }

            // 3. Insert transaction
            psTxn = conn.prepareStatement(insertTxn);
            psTxn.setInt(1, txn.getUserId());
            psTxn.setInt(2, txn.getAccountId());
            psTxn.setString(3, txn.getTxnType());
            psTxn.setBigDecimal(4, txn.getAmount());
            psTxn.setString(5, txn.getDescription());
            psTxn.setBigDecimal(6, newBalance);
            psTxn.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));

            int inserted = psTxn.executeUpdate();

            if (inserted > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ignore) {}
            return false;
        } finally {
            try { if (psBalance != null) psBalance.close(); } catch (SQLException ignore) {}
            try { if (psTxn != null) psTxn.close(); } catch (SQLException ignore) {}
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (SQLException ignore) {}
        }
    }


    public List<Transaction> getTransactionsByUser(int userId) {
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM transactions WHERE user_id=? ORDER BY txn_time DESC LIMIT 10")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTxnId(rs.getInt("txn_id"));
                t.setUserId(rs.getInt("user_id"));
                t.setAccountId(rs.getInt("account_id"));
                t.setTxnType(rs.getString("txn_type"));
                t.setAmount(rs.getBigDecimal("amount"));
                t.setDescription(rs.getString("description"));
                t.setTxnTime(rs.getTimestamp("txn_time"));
                t.setBalanceAfter(rs.getBigDecimal("balance_after"));
                t.setChannel(rs.getString("channel"));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Account> getAllActiveOtherAccounts(int currentUserId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT user_id, username, account_type " +
                     "FROM users WHERE user_id != ? AND status = 'APPROVED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Account acc = new Account();
                acc.setAccountId(rs.getInt("user_id"));
                acc.setAccountNumber("ACC" + rs.getInt("user_id")); // or real account_no if exists
                acc.setAccountHolderName(rs.getString("username"));
                acc.setAccountType(rs.getString("account_type"));
                accounts.add(acc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }
    
    public Map<String, BigDecimal> getTransactionSummary(int accountId, String type, String monthParam, String fromDate, String toDate) {
        Map<String, BigDecimal> summary = new LinkedHashMap<>();
        // Initialize all months to 0
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for(String m: months) summary.put(m, BigDecimal.ZERO);

        String sql = "SELECT MONTH(txn_time) AS month, SUM(amount) AS total " +
                     "FROM transactions WHERE account_id=? AND txn_type=? ";

        if(monthParam != null && !monthParam.isEmpty()) {
            sql += " AND YEAR(txn_time)=? AND MONTH(txn_time)=?";
        } else if(fromDate != null && !fromDate.isEmpty() && toDate != null && !toDate.isEmpty()) {
            sql += " AND txn_time BETWEEN ? AND ?";
        }

        sql += " GROUP BY MONTH(txn_time) ORDER BY MONTH(txn_time)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ps.setString(2, type);

            if(monthParam != null && !monthParam.isEmpty()) {
                String[] arr = monthParam.split("-");
                ps.setInt(3, Integer.parseInt(arr[0])); // year
                ps.setInt(4, Integer.parseInt(arr[1])); // month
            } else if(fromDate != null && !fromDate.isEmpty() && toDate != null && !toDate.isEmpty()) {
                ps.setString(3, fromDate + " 00:00:00");
                ps.setString(4, toDate + " 23:59:59");
            }

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int monthIndex = rs.getInt("month") - 1;
                BigDecimal total = rs.getBigDecimal("total");
                summary.put(months[monthIndex], total);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return summary;
    }
    
    public double getTotalByTypeLastMonth(int userId, String type) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount),0) " +
                     "FROM transactions " +
                     "WHERE user_id = ? AND txn_type = ? " +
                     "AND txn_time >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }


}