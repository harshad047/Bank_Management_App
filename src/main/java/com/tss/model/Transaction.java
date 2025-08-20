package com.tss.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int txnId;
    private int userId;
    private int accountId;
    private String txnType;
    private BigDecimal amount;
    private String description;
    private Timestamp txnTime;
    private BigDecimal balanceAfter;
    private String channel;

    // Getters & Setters
    public int getTxnId() { return txnId; }
    public void setTxnId(int txnId) { this.txnId = txnId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getTxnType() { return txnType; }
    public void setTxnType(String txnType) { this.txnType = txnType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Timestamp getTxnTime() { return txnTime; }
    public void setTxnTime(Timestamp txnTime) { this.txnTime = txnTime; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal amount) { this.balanceAfter = amount; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
}
