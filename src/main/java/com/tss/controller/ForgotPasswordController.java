// src/main/java/com/tss/controller/ForgotPasswordController.java

package com.tss.controller;

import com.tss.service.SecurityQuestionService;
import com.tss.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {

    private UserService userService = new UserService();
    private SecurityQuestionService securityQuestionService = new SecurityQuestionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("questions", securityQuestionService.getAllQuestions());
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("verify".equals(action)) {
            String username = request.getParameter("username");
            String questionIdStr = request.getParameter("questionId");
            String answer = request.getParameter("answer");

            // Validate input
            if (username == null || username.trim().isEmpty() ||
                questionIdStr == null || answer == null || answer.trim().isEmpty()) {
                request.setAttribute("error", "All fields are required.");
                doGet(request, response);
                return;
            }

            int questionId;
            try {
                questionId = Integer.parseInt(questionIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid security question selected.");
                doGet(request, response);
                return;
            }

            boolean isValid = userService.verifySecurityAnswer(username, questionId, answer);
            if (isValid) {
                session.setAttribute("resetUsername", username);
                request.setAttribute("showReset", true);
                request.setAttribute("questions", securityQuestionService.getAllQuestions());
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Incorrect answer. Please try again.");
                request.setAttribute("questions", securityQuestionService.getAllQuestions());
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            }

        } else if ("reset".equals(action)) {
            String resetUsername = (String) session.getAttribute("resetUsername");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");

            if (resetUsername == null) {
                response.sendRedirect("login");
                return;
            }

            if (newPassword == null || newPassword.length() < 6) {
                request.setAttribute("error", "Password must be at least 6 characters long.");
                request.setAttribute("showReset", true);
                request.setAttribute("questions", securityQuestionService.getAllQuestions());
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                request.setAttribute("error", "Passwords do not match.");
                request.setAttribute("showReset", true);
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                return;
            }

            boolean success = userService.changePassword(resetUsername, newPassword);
            if (success) {
                session.removeAttribute("resetUsername");
                request.setAttribute("success", "Password changed successfully. You can now login.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to change password. Please try again.");
                request.setAttribute("showReset", true);
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("forgot-password");
        }
    }
}