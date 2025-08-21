package com.tss.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FixedDeposit {
    private int fdId;
    private int fdAppId;
    private int userId;
    private int accountId;
    private double amount;
    private int tenureMonths;
    private double interestRate;
    private double maturityAmount;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public int getFdId() { return fdId; }
    public void setFdId(int fdId) { this.fdId = fdId; }

    public int getFdAppId() { return fdAppId; }
    public void setFdAppId(int fdAppId) { this.fdAppId = fdAppId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public double getMaturityAmount() { return maturityAmount; }
    public void setMaturityAmount(double maturityAmount) { this.maturityAmount = maturityAmount; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}