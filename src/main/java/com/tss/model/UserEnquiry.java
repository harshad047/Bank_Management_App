package com.tss.model;

import java.sql.Timestamp;

public class UserEnquiry {
    private int enquiryId;
    private int userId;
    private String queryType;
    private String description;
    private String status;
    private Timestamp submittedAt;
    private Timestamp resolvedAt;
    private String adminResponse;

    // Optional: to show username in admin table
    private User user;

    // Getters and setters
    public int getEnquiryId() { return enquiryId; }
    public void setEnquiryId(int enquiryId) { this.enquiryId = enquiryId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getQueryType() { return queryType; }
    public void setQueryType(String queryType) { this.queryType = queryType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Timestamp submittedAt) { this.submittedAt = submittedAt; }

    public Timestamp getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Timestamp resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
