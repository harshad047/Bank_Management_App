package com.tss.model;

public class Beneficiary {
    private int beneficiaryId;
    private int userId;
    private int accountId;         // user’s own account
    private int beneficiaryAccId;  // internal beneficiary account
    private String nickname;
    private boolean active;

    // Getters & Setters
    public int getBeneficiaryId() { return beneficiaryId; }
    public void setBeneficiaryId(int beneficiaryId) { this.beneficiaryId = beneficiaryId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getBeneficiaryAccId() { return beneficiaryAccId; }
    public void setBeneficiaryAccId(int beneficiaryAccId) { this.beneficiaryAccId = beneficiaryAccId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
