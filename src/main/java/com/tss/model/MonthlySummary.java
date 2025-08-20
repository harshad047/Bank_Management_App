package com.tss.model;
// src/main/java/com/tss/model/MonthlySummary.java

public class MonthlySummary {
    private int year;
    private int month;
    private java.math.BigDecimal totalCredit;
    private java.math.BigDecimal totalDebit;

    // Getters and Setters
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public java.math.BigDecimal getTotalCredit() { return totalCredit; }
    public void setTotalCredit(java.math.BigDecimal totalCredit) { this.totalCredit = totalCredit; }

    public java.math.BigDecimal getTotalDebit() { return totalDebit; }
    public void setTotalDebit(java.math.BigDecimal totalDebit) { this.totalDebit = totalDebit; }

    public String getLabel() {
        return getMonthName(month) + " " + year;
    }

    private String getMonthName(int month) {
        String[] names = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return names[month - 1];
    }
}