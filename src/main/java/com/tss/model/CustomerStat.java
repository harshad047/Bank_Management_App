package com.tss.model;

import java.math.BigDecimal;

public class CustomerStat {
    private int userId;
    private String userName;
    private BigDecimal balance;
    private int transactionCount;

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getTransactionCount() {
        return transactionCount;
    }
    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }
}
