package com.tss.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transfer {
    private int transferId;
    private int fromAccountId;
    private int toAccountId;
    private BigDecimal amount;
    private String description;
    private Timestamp transferTime;

    // Getters and Setters
    public int getTransferId() {
        return transferId;
    }
    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }
    public int getFromAccountId() {
        return fromAccountId;
    }
    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }
    public int getToAccountId() {
        return toAccountId;
    }
    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Timestamp getTransferTime() {
        return transferTime;
    }
    public void setTransferTime(Timestamp transferTime) {
        this.transferTime = transferTime;
    }
}
