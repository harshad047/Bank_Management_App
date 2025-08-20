// src/main/java/com/tss/controller/AdminDashboardController.java
package com.tss.controller;

import com.tss.model.User;
import com.tss.model.AdminApproval;
import com.tss.service.UserService;
import com.tss.service.AdminApprovalService;
import com.tss.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/*")
public class AdminDashboardController extends HttpServlet {

    private UserService userService = new UserService();
    private AdminApprovalService approvalService = new AdminApprovalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Check if admin logged in
        if (!SessionUtils.isAdmin(request)) {
            response.sendRedirect("../login");
            return;
        }

        String path = request.getPathInfo();
        if (path == null || "/".equals(path)) {
            path = "/dashboard";
        }

        // Load all users
        List<User> users = Collections.emptyList();
        try {
            users = userService.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load user data.");
            request.getRequestDispatcher("/admin/error.jsp").forward(request, response);
            return;
        }

        // KPIs
        long totalUsers = users.size();
        long approvedCount = users.stream().filter(u -> "APPROVED".equals(u.getStatus())).count();
        long pendingCount = users.stream().filter(u -> "PENDING".equals(u.getStatus())).count();
        long rejectedCount = users.stream().filter(u -> "REJECTED".equals(u.getStatus())).count();

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("approvedCount", approvedCount);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("rejectedCount", rejectedCount);

        switch (path) {
            case "/dashboard":
                // 📝 Fetch today approvals once
                List<AdminApproval> approvals = approvalService.getTodayApprovals();

                // 🔍 Apply filter (from query param: ?filter=APPROVED / REJECTED / ALL)
                String filter = request.getParameter("filter");
                if (filter != null && !"ALL".equalsIgnoreCase(filter)) {
                    approvals = approvals.stream()
                            .filter(a -> filter.equalsIgnoreCase(a.getAction()))
                            .collect(Collectors.toList());
                }

                request.setAttribute("todayApprovals", approvals);
                request.setAttribute("selectedFilter", filter == null ? "ALL" : filter);

                request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
                break;

            case "/approve-users":
                List<User> pendingUsers = users.stream()
                        .filter(u -> "PENDING".equals(u.getStatus()))
                        .collect(Collectors.toList());
                request.setAttribute("pendingUsers", pendingUsers);
                request.getRequestDispatcher("/approve-users.jsp").forward(request, response);
                break;

            default:
                response.sendError(404, "Page not found: " + path);
        }
    }
}
