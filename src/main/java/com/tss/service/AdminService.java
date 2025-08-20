package com.tss.service;

import com.tss.dao.AdminDAO;
import com.tss.model.Admin;
import com.tss.util.PasswordUtil;

import java.sql.SQLException;

public class AdminService {

    private AdminDAO adminDAO = new AdminDAO();

    // Authenticate admin
    public Admin login(String username, String password) {
        try {
            Admin admin = adminDAO.findByUsername(username);
            if (admin != null && admin.getPassword().equals(password)) { // plain text compare
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add new admin (only by super admin)
    public String addAdmin(Admin admin, boolean isSuperAdminRequester) {
        try {
            if (!isSuperAdminRequester) {
                return "Only super admin can add new admins.";
            }

            if (adminDAO.existsByUsernameOrEmail(admin.getUsername(), admin.getEmail())) {
                return "Username or email already exists.";
            }

            // Hash password later if needed
            admin.setSuperAdmin(admin.isSuperAdmin()); // role set by form

            boolean success = adminDAO.createAdmin(admin);
            return success ? "SUCCESS" : "Failed to create admin.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error occurred.";
        }
    }
}