// src/main/java/com/tss/controller/ManageUsersServlet.java
package com.tss.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;   // ✅ Missing import
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.service.UserService;
import com.tss.util.SessionUtils;
import com.tss.model.User;

@WebServlet("/admin/manage-users")
public class ManageUsersServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Check if admin is logged in
        if (!SessionUtils.isAdmin(request)) {
            response.sendRedirect("../login");
            return;
        }

        // 🔍 Get search query
        String search = request.getParameter("search");
        List<User> users;

        try {
            if (search != null && !search.trim().isEmpty()) {
                users = userService.searchUsers(search.trim());
            } else {
                users = userService.getAllUsers();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load user data from database.");
            users = java.util.Collections.emptyList();
        }

        // 📤 Forward to JSP
        request.setAttribute("users", users);
        request.getRequestDispatcher("/manage-users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Admin only
        if (!SessionUtils.isAdmin(request)) {
            response.sendRedirect("../login");
            return;
        }

        String action = request.getParameter("action");
        String userIdParam = request.getParameter("userId");

        System.out.println("👉 Action received: " + action);
        System.out.println("👉 userIdParam received: " + userIdParam);

        if (action == null || userIdParam == null || userIdParam.isEmpty()) {
            request.setAttribute("error", "Invalid request. Missing parameters.");
            reloadUsers(request);
            request.getRequestDispatcher("/manage-users.jsp").forward(request, response);
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdParam.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid User ID format.");
            reloadUsers(request);
            request.getRequestDispatcher("/manage-users.jsp").forward(request, response);
            return;
        }

        int adminId = SessionUtils.getAdminId(request);
        boolean success = false;

        if ("delete".equalsIgnoreCase(action)) {

            if (userService.isUserInactive(userId)) {
                request.setAttribute("error", "⚠️ User is already deactivated or rejected.");
            } else {
                success = userService.deactivateUser(userId, adminId);

                if (success) {
                    request.setAttribute("success", "✅ User has been successfully deactivated.");
                } else {
                    request.setAttribute("error", "❌ Failed to deactivate user. User may not exist.");
                }
            }

        } 
        else if ("edit".equalsIgnoreCase(action)) {
            String newStatus = request.getParameter("newStatus");

            if (newStatus == null || newStatus.isEmpty()) {
                request.setAttribute("error", "❌ No status selected for update.");
            } else {
                User existingUser = userService.findByUserId(userId);
                if (existingUser == null) {
                    request.setAttribute("error", "❌ User not found.");
                } else {
                    String currentStatus = existingUser.getStatus();
                    boolean updated = false;

                    if ("APPROVED".equalsIgnoreCase(currentStatus)) {
                        request.setAttribute("error", "⚠️ User is already active.");
                    }
                    else if ("REJECTED".equalsIgnoreCase(currentStatus) && "APPROVED".equalsIgnoreCase(newStatus)) {
                        // ✅ REJECTED → APPROVED
                    	System.out.println("Editing Rjected");
                        updated = userService.approveUser(userId, adminId, "Approved after rejection by admin");
                    }
                    else if ("DEACTIVATED".equalsIgnoreCase(currentStatus) && "APPROVED".equalsIgnoreCase(newStatus)) {
                        // ✅ DEACTIVATED → APPROVED
                        updated = userService.updateUserStatus(userId, "APPROVED", adminId);
                    }
                    else {
                        request.setAttribute("error", "❌ Invalid status transition: " + currentStatus + " → " + newStatus);
                    }

                    if (updated) {
                        request.setAttribute("success", "✅ User status updated to " + newStatus + ".");
                    } else {
                        if (request.getAttribute("error") == null) {
                            request.setAttribute("error", "❌ Failed to update user status.");
                        }
                    }
                }
            }
        }

        else {
            request.setAttribute("error", "Unknown action: " + action);
        }


        reloadUsers(request);

        request.getRequestDispatcher("/manage-users.jsp").forward(request, response);
    }

    /**
     * Utility method to reload user list safely
     */
    private void reloadUsers(HttpServletRequest request) {
        try {
            request.setAttribute("users", userService.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Could not reload user list.");
            request.setAttribute("users", java.util.Collections.emptyList());
        }
    }
}
