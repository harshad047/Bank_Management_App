// src/main/java/com/tss/service/UserService.java

package com.tss.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.tss.dao.AccountDAO;
import com.tss.dao.AdminApprovalDAO;
import com.tss.dao.UserDAO;
import com.tss.dao.UserSecurityAnswerDAO;
import com.tss.database.DBConnection;
import com.tss.model.Account;
import com.tss.model.User;
import com.tss.util.Validator;

public class UserService {

    private UserDAO userDAO = new UserDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private UserSecurityAnswerDAO answerDAO = new UserSecurityAnswerDAO();
    private AdminApprovalDAO approvalDAO = new AdminApprovalDAO();

    // Step 1: Register User (PENDING) - Insert or Reactivate
    public String registerUser(User user, int securityQuestionId, String securityAnswer) {
        try {
            // Validate inputs
            if (!Validator.isValidEmail(user.getEmail())) {
                return "Invalid email format.";
            }
            if (user.getPassword().length() < 6) {
                return "Password must be at least 6 characters long.";
            }

            // Check if username or email already exists (active)
            User existingUser = userDAO.findByUsername(user.getUsername());
            if (existingUser != null && "APPROVED".equals(existingUser.getStatus())) {
                return "User with this username already has an active account.";
            }
            existingUser = userDAO.findByEmail(user.getEmail());
            if (existingUser != null && "APPROVED".equals(existingUser.getStatus())) {
                return "User with this email already has an active account.";
            }

            // Save or update user
            boolean success = userDAO.saveOrUpdate(user);
            if (!success) {
                return "Failed to register user.";
            }

            // Save security answer
            if (!answerDAO.saveAnswer(user.getUserId(), securityQuestionId, securityAnswer)) {
                return "Failed to save security answer.";
            }

            return "SUCCESS";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error occurred.";
        }
    }

 // com.tss.service.UserService.java

 // Approve user: create account, update status, log approval
 public boolean approveUser(int userId, int adminId, String reason) {
     Connection conn = null;
     try {
         conn = DBConnection.getConnection();
         conn.setAutoCommit(false);

         User user = userDAO.findByUserId(userId);
         if (user == null ) {
             return false;
         }

         // Update user status
         user.setStatus("APPROVED");
         user.setApprovedBy(adminId);
         user.setApprovedAt(new java.util.Date());

         if (!userDAO.updateApprovedUser(user)) {
             conn.rollback();
             return false;
         }

         // Create account
         Account acc = new Account();
         acc.setUserId(user.getUserId());
         acc.setAccountNumber(accountDAO.generateAccountNumber());
         acc.setBalance(BigDecimal.ZERO);
         if (!accountDAO.createAccount(acc)) {
             conn.rollback();
             return false;
         }

         // Log approval
         if (!approvalDAO.logApproval(userId, adminId, "APPROVED", reason)) {
             conn.rollback();
             return false;
         }

         conn.commit();
         return true;
     } catch (SQLException e) {
         try { conn.rollback(); } catch (Exception ex) {}
         e.printStackTrace();
         return false;
     } finally {
         if (conn != null) {
             try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { }
         }
     }
 }

 // Reject user
 public boolean rejectUser(int userId, int adminId, String reason) {
     try (Connection conn = DBConnection.getConnection()) {
         conn.setAutoCommit(false);
         try {
             // Update user status
             String sql = "UPDATE users SET status = 'REJECTED', rejection_reason = ? WHERE user_id = ? AND status = 'PENDING'";
             try (PreparedStatement ps = conn.prepareStatement(sql)) {
                 ps.setString(1, reason);
                 ps.setInt(2, userId);
                 if (ps.executeUpdate() == 0) {
                     conn.rollback();
                     return false;
                 }
             }

             // Log rejection
             if (!new AdminApprovalDAO().logApproval(userId, adminId, "REJECTED", reason)) {
                 conn.rollback();
                 return false;
             }

             conn.commit();
             return true;
         } catch (SQLException e) {
             conn.rollback();
             throw e;
         }
     } catch (SQLException e) {
         e.printStackTrace();
         return false;
     }
 }

    // Verify security answer for forgot password
    public boolean verifySecurityAnswer(String username, int questionId, String answer) {
        try {
            User user = userDAO.findByUsername(username);
            if (user == null) return false;
            return answerDAO.verifyAnswer(user.getUserId(), questionId, answer);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Change password
    public boolean changePassword(String username, String newPassword) {
        try {
            String sql = "UPDATE users SET password = ? WHERE username = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, newPassword); // plain text
                ps.setString(2, username);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all users
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }
    
 // Add this method to com.tss.service.UserService

    public User findByUsername(String username) {
        try {
            return userDAO.findByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
 // com.tss.service.UserService.java

    public User findByUserId(int userId) {
        try {
            return userDAO.findByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<User> searchUsers(String query) throws SQLException {
        return userDAO.searchUsers(query);
    }
    
    public boolean deactivateUser(int userId, int adminId) {
        try {
            return userDAO.deactivateUser(userId, adminId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isUserInactive(int userId) {
        return userDAO.isUserInactive(userId);
    }

 // com.tss.service.UserService.java

    public long getNewRegistrationsToday() {
        try {
            return userDAO.getNewRegistrationsToday();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

	public Integer getAccountIdByUserId(int userId) {
		return accountDAO.getAccountIdByUserId(userId);
	}

	public boolean updateUser(User user) {
		return userDAO.updateUser(user);
	}

	public boolean updateUserStatus(int userId, String newStatus, int adminId) {
	    try {
	        return userDAO.updateUserStatus(userId, newStatus, adminId);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}