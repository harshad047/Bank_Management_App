// src/main/java/com/tss/service/PassbookService.java
package com.tss.service;

import com.tss.dao.PassbookDAO;
import com.tss.model.Account;
import com.tss.model.Transaction;

import java.util.List;

public class PassbookService {
    private PassbookDAO passbookDAO = new PassbookDAO();

    public Account getAccount(int userId) {
        return passbookDAO.getAccountByUserId(userId);
    }

    public List<Transaction> getRecentTransactions(int userId, int limit) {
        return passbookDAO.getRecentTransactions(userId, limit);
    }
}
