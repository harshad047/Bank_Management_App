// src/main/java/com/tss/model/AdminApproval.java
package com.tss.model;

import java.sql.Timestamp;

public class AdminApproval {
    private int approvalId;
    private int userId;
    private int adminId;
    private String action;
    private String reason;
    private Timestamp createdAt;

    // getters & setters
    public int getApprovalId() { return approvalId; }
    public void setApprovalId(int approvalId) { this.approvalId = approvalId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
