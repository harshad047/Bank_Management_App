// src/main/java/com/tss/controller/LoginController.java

package com.tss.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.model.Account;
import com.tss.model.Admin;
import com.tss.model.User;
import com.tss.service.AdminService;
import com.tss.service.UserService;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private UserService userService = new UserService();
    private AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Clear any previous errors
        request.removeAttribute("error");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("[DEBUG] Login Attempt - Username: " + username);

        if (username == null || password == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both username and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        username = username.trim();

        // Try User Login
        User user = userService.findByUsername(username);

        if (user != null) {
            System.out.println("[DEBUG] User Found: " + user.getUsername() + ", Status: " + user.getStatus());

            if ("APPROVED".equals(user.getStatus())) {
                if (password.equals(user.getPassword())) {
                    HttpSession session = request.getSession();
                    
                    session.setAttribute("role", "user");
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("email", user.getEmail());
                    session.setAttribute("phone", user.getPhone());
                    session.setAttribute("accountType", user.getAccountType());
                    session.setAttribute("status", user.getStatus());
                    
                    System.out.println("[DEBUG] User Login Success");
                    Integer accountId = userService.getAccountIdByUserId(user.getUserId());
                    
                    if (accountId != null) {
                        session.setAttribute("accountId", accountId);
                    } else {
                        System.out.println("[WARN] No accountId found for userId: " + user.getUserId());
                    }
                    response.sendRedirect(request.getContextPath() +"/user/dashboard");  // ✅ Fixed: No .jsp
                    return;
                } else {
                    request.setAttribute("error", "Invalid password.");
                }
            } else {
                request.setAttribute("error", getStatusMessage(user.getStatus(), user.getRejectionReason()));
            }
        }

        // Try Admin Login
        Admin admin = adminService.login(username, password);
        if (admin != null) {
            HttpSession session = request.getSession();
            session.setAttribute("role", "admin");
            session.setAttribute("adminId", admin.getAdminId());
            session.setAttribute("username", admin.getUsername());
            System.out.println("[DEBUG] Admin Login Success: " + username);
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        // Final fallback
        if (request.getAttribute("error") == null) {
            request.setAttribute("error", "Invalid username or password.");
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private String getStatusMessage(String status, String reason) {
        return switch (status) {
            case "PENDING" -> "Your account is pending admin approval.";
            case "REJECTED" -> "Your account was rejected: " + (reason != null ? reason : "No reason provided.");
            case "DEACTIVATED" -> "Your account has been deactivated.";
            default -> "Access denied.";
        };
    }
}