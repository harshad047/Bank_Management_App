package com.tss.service;

import java.math.BigDecimal;
import java.util.List;

import com.tss.dao.TransferDao;
import com.tss.model.Account;
import com.tss.model.Transfer;

public class TransferService {
    private TransferDao transferDao = new TransferDao();

    public boolean transfer(int fromAccountId, int fromUserId, int toAccountId, int toUserId, BigDecimal amount, String description) throws Exception {
       
        return transferDao.makeTransfer(fromUserId,fromAccountId, toUserId,toAccountId, amount, description);
    }
    public List<Transfer> getTransfers(int accountId) {
        return transferDao.getTransfersByAccount(accountId);
    }
    
    public List<Account> getActiveAccountsExcluding(int currentAccountId) {
        return transferDao.getAllActiveAccountsExcluding(currentAccountId);
    }
}
