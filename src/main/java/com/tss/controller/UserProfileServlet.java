package com.tss.controller;

import com.tss.model.User;
import com.tss.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/user-profile")
public class UserProfileServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"user".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // No DB query needed here since user details are in session
        request.getRequestDispatcher("/user-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"user".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        // Fetch updated fields from form
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password"); // optional

        // Prepare User object for update
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setPhone(phone);
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(password);
        }

        // Update in database
        boolean updated = userService.updateUser(user);

        if (updated) {
            // Update session attributes
            session.setAttribute("email", email);
            session.setAttribute("phone", phone);
            if (password != null && !password.trim().isEmpty()) {
                session.setAttribute("password", password);
            }

            request.setAttribute("success", "Profile updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update profile.");
        }

        request.getRequestDispatcher("/user-profile.jsp").forward(request, response);
    }
}
