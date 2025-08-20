package com.tss.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tss.dao.TransactionDAO;
import com.tss.model.MonthlySummary;
import com.tss.model.Transaction;

public class TransactionService {

    private TransactionDAO transactionDAO = new TransactionDAO();

    public boolean recordTransaction(Transaction txn) {
        try {
            return transactionDAO.insertTransaction(txn);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException {
        return transactionDAO.getTransactionsByUserId(userId);
    }

    public List<Transaction> filterByDateRange(int userId, java.util.Date from, java.util.Date to) throws SQLException {
        return transactionDAO.getTransactionsByDateRange(userId, from, to);
    }

    public List<Transaction> filterByAmountRange(int userId, BigDecimal min, BigDecimal max) throws SQLException {
        return transactionDAO.getTransactionsByAmountRange(userId, min, max);
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionDAO.getAllTransactions();
    }
    
 // com.tss.service.TransactionService.java

    public long getTotalTransactionCount() {
        try {
            return transactionDAO.getTotalTransactionCount();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    

    public List<Transaction> getRecentTransactions(int limit) {
        try {
            return transactionDAO.getRecentTransactions(limit);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
 // src/main/java/com/tss/service/TransactionService.java

//    public List<MonthlySummary> getMonthlySummary(int userId) {
//        try {
//            return transactionDAO.getMonthlySummary(userId);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
//    
 // com.tss.service.TransactionService.java

    public List<MonthlySummary> getMonthlySummaryForAllUsers() {
        try {
            return transactionDAO.getMonthlySummaryForAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
 // com.tss.service.TransactionService.java
    public List<Transaction> getTransactionsWithFilters(
            BigDecimal minAmount, BigDecimal maxAmount,
            java.util.Date fromDate, java.util.Date toDate,
            String accountType) {
        try {
            return transactionDAO.getTransactionsWithFilters(minAmount, maxAmount, fromDate, toDate, accountType);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public boolean credit(int userId, int accountId, BigDecimal amount, String description) {
        Transaction txn = new Transaction();
        txn.setUserId(userId);
        txn.setAccountId(accountId);
        txn.setTxnType("CREDIT");
        txn.setAmount(amount);
        txn.setDescription(description);
        return transactionDAO.addTransaction(txn);
    }

    public boolean debit(int userId, int accountId, BigDecimal amount, String description) {
        Transaction txn = new Transaction();
        txn.setUserId(userId);
        txn.setAccountId(accountId);
        txn.setTxnType("DEBIT");
        txn.setAmount(amount);
        txn.setDescription(description);
        return transactionDAO.addTransaction(txn);
    }


    public List<Transaction> getTransactionsByUser(int userId) {
        return transactionDAO.getTransactionsByUser(userId);
    }

    public Map<String, BigDecimal> getMonthlyCredits(int accountId, String monthParam, String fromDate, String toDate) {
        return transactionDAO.getTransactionSummary(accountId, "CREDIT", monthParam, fromDate, toDate);
    }

    public Map<String, BigDecimal> getMonthlyDebits(int accountId, String monthParam, String fromDate, String toDate) {
        return transactionDAO.getTransactionSummary(accountId, "DEBIT", monthParam, fromDate, toDate);
    }
    
    public double getTotalByTypeLastMonth(int userId, String type) throws SQLException {
        return transactionDAO.getTotalByTypeLastMonth(userId, type);
    }

}