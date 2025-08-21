// src/main/java/com/tss/service/AdminApprovalService.java
package com.tss.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.tss.dao.AdminApprovalDAO;
import com.tss.model.AdminApproval;

public class AdminApprovalService {

    private AdminApprovalDAO approvalDao = new AdminApprovalDAO();

    /**
     * Get today’s approvals only.
     */
    public List<AdminApproval> getTodayApprovals() {
        try {
            return approvalDao.getTodayApprovals();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Get last N approvals (regardless of date).
     */
    public List<AdminApproval> getRecentApprovals(int limit) {
        try {
            return approvalDao.getRecentApprovals(limit);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public boolean logApproval(int userId, int adminId, String action, String reason)
    {
    	try {
			return approvalDao.logApproval(userId, adminId, action, reason);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    }
}
