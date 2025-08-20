// src/main/java/com/tss/service/AccountService.java

package com.tss.service;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.tss.dao.AccountDAO;
import com.tss.model.Account;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAO();

    // Get account by user ID
    public Account findByUserId(int userId) {
        try {
            return accountDAO.findByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Create a new account for a user
    public boolean createAccount(int userId) {
        try {
            Account acc = new Account();
            acc.setUserId(userId);
            acc.setAccountNumber(accountDAO.generateAccountNumber());
            acc.setBalance(java.math.BigDecimal.ZERO);
            return accountDAO.createAccount(acc);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    // Get account by account number (optional)
//    public Account findByAccountNumber(String accountNumber) {
//        try {
//            return accountDAO.existsByAccountNumber(accountNumber);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

 // com.tss.service.AccountService.java

    public BigDecimal getTotalBalance() {
        try {
            return accountDAO.getTotalBalance();
        } catch (SQLException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    // Update account balance (used in transactions)
    public boolean updateBalance(int accountId, java.math.BigDecimal newBalance) {
        try {
            return accountDAO.updateBalance(accountId, newBalance);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}