package com.tss.service;

import java.math.BigDecimal;
import java.util.List;

import com.tss.dao.TransferDao;
import com.tss.model.Account;
import com.tss.model.Transfer;

public class TransferService {
    private TransferDao transferDao = new TransferDao();

    public boolean transfer(int fromAccountId, int toAccountId, BigDecimal amount, String description) throws Exception {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(fromAccountId);
        transfer.setToAccountId(toAccountId);
        transfer.setAmount(amount);
        transfer.setDescription(description);
        return transferDao.makeTransfer(transfer);
    }


    public List<Transfer> getTransfers(int accountId) {
        return transferDao.getTransfersByAccount(accountId);
    }
    
    public List<Account> getActiveAccountsExcluding(int currentAccountId) {
        return transferDao.getAllActiveAccountsExcluding(currentAccountId);
    }
}
