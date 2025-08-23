package com.tss.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserFdView {
    private int fdAppId;
    private int fdId; // null for pending
    private String type; // "APPLICATION" or "FD"
    private String status; // from enum (PENDING, APPROVED, REJECTED, ACTIVE, MATURED, CLOSED)
    private double amount;
    private int tenureMonths;
    private double interestRate;

    // For FD
    private LocalDate startDate;
    private LocalDate maturityDate;
    private double maturityAmount;

    // For Application
    private LocalDateTime applicationDate;
    private String rejectionReason;

    // Constructors
    // Application only
    public UserFdView(int fdAppId, double amount, int tenureMonths, double interestRate,
                      LocalDateTime applicationDate, String status, String rejectionReason) {
        this.type = "APPLICATION";
        this.fdAppId = fdAppId;
        this.amount = amount;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.applicationDate = applicationDate;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    // FD only
    public UserFdView(int fdId, int fdAppId, double amount, int tenureMonths, double interestRate,
                      LocalDate startDate, LocalDate maturityDate, double maturityAmount, String status) {
        this.type = "FD";
        this.fdId = fdId;
        this.fdAppId = fdAppId;
        this.amount = amount;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.maturityDate = maturityDate;
        this.maturityAmount = maturityAmount;
        this.status = status;
    }

    // Getters
    public int getFdAppId() { return fdAppId; }
    public int getFdId() { return fdId; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public double getAmount() { return amount; }
    public int getTenureMonths() { return tenureMonths; }
    public double getInterestRate() { return interestRate; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public double getMaturityAmount() { return maturityAmount; }
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public String getRejectionReason() { return rejectionReason; }
}
