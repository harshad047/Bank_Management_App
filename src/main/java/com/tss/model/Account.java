package com.tss.model;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int userId;
    private String accountNumber;
    private BigDecimal balance;
    private java.util.Date createdAt;

    // 🔹 Extra fields for dropdown & transfer
    private String accountHolderName;  // from users.username
    private String accountType;        // from users.account_type
    private String status;             // from users.status (APPROVED / REJECTED / DEACTIVATED)

    // Getters and Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public java.util.Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.util.Date createdAt) { this.createdAt = createdAt; }

    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
