// src/main/java/com/tss/dto/UserEnquiry.java
package com.tss.dto;

import java.time.LocalDateTime;

public class UserEnquiry {
    private int enquiryId;
    private int userId;
    private String queryType;
    private String description;
    private String status;
    private LocalDateTime submittedAt;
    private LocalDateTime resolvedAt;
    private String adminResponse;

    // Default constructor
    public UserEnquiry() {}

    // Parameterized constructor
    public UserEnquiry(int userId, String queryType, String description) {
        this.userId = userId;
        this.queryType = queryType;
        this.description = description;
    }

    // Getters and Setters
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

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }
}