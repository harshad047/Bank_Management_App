package com.tss.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserFdView {
    private String type; // "ACTIVE_FD" or "PENDING_APPLICATION"
    private double amount;
    private int tenureMonths;
    private double interestRate;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private double maturityAmount;
    private LocalDateTime applicationDate;

    // Constructor for Approved FD
    public UserFdView(double amount, int tenureMonths, double interestRate,
                      LocalDate startDate, LocalDate maturityDate, double maturityAmount) {
        this.type = "ACTIVE_FD";
        this.amount = amount;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.maturityDate = maturityDate;
        this.maturityAmount = maturityAmount;
    }

    // Constructor for Pending Application
    public UserFdView(double amount, int tenureMonths, double interestRate, LocalDateTime applicationDate) {
        this.type = "PENDING_APPLICATION";
        this.amount = amount;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.applicationDate = applicationDate;
    }

    // Getters
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public int getTenureMonths() { return tenureMonths; }
    public double getInterestRate() { return interestRate; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public double getMaturityAmount() { return maturityAmount; }
    public LocalDateTime getApplicationDate() { return applicationDate; }

    public String getStatusLabel() {
        return "PENDING".equals(type) ? "Pending Approval" : "Active";
    }
}