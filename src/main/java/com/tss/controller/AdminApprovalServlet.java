// src/main/java/com/tss/controller/AdminApprovalServlet.java

package com.tss.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tss.service.UserService;
import com.tss.util.SessionUtils;

@WebServlet("/admin/approve")
public class AdminApprovalServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Only admin can approve
        if (!SessionUtils.isAdmin(request)) {
            response.sendRedirect("../login");
            return;
        }

        String action = request.getParameter("action");
        int userId = Integer.parseInt(request.getParameter("userId"));
        int adminId = SessionUtils.getAdminId(request);
        String reason = request.getParameter("reason");

        boolean success = false;

        if ("approve".equals(action)) {
            success = userService.approveUser(userId, adminId, "Approved by admin");
            if (success) {
                request.setAttribute("success", "User approved successfully.");
            } else {
                request.setAttribute("error", "Failed to approve user.");
            }
        } else if ("reject".equals(action)) {
            if (reason == null || reason.trim().isEmpty()) {
                reason = "No reason provided";
            }
            success = userService.rejectUser(userId, adminId, reason);
            if (success) {
                request.setAttribute("success", "User rejected successfully.");
            } else {
                request.setAttribute("error", "Failed to reject user.");
            }
        }
        

        // Redirect back to approve-users page
        response.sendRedirect("approve-users");
    }
}