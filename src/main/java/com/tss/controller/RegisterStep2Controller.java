// src/main/java/com/tss/controller/RegisterStep2Controller.java

package com.tss.controller;

import com.tss.model.User;
import com.tss.service.SecurityQuestionService;
import com.tss.service.UserService;
import com.tss.util.Validator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register/step2")
public class RegisterStep2Controller extends HttpServlet {

    private UserService userService = new UserService();
    private SecurityQuestionService securityQuestionService = new SecurityQuestionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("questions", securityQuestionService.getAllQuestions());
            request.getRequestDispatcher("/register/step2.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("step1");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("tempAccountType") == null) {
            response.sendRedirect("step1");
            return;
        }

        // Get form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        String answer = request.getParameter("answer");

        // Validate
        if (!Validator.isValidUsername(username)) {
            request.setAttribute("error", "Invalid username.");
            loadAndForward(request, response);
            return;
        }
        if (!Validator.isValidPassword(password)) {
            request.setAttribute("error", "Password must be at least 6 characters.");
            loadAndForward(request, response);
            return;
        }
        if (!Validator.isValidEmail(email)) {
            request.setAttribute("error", "Invalid email.");
            loadAndForward(request, response);
            return;
        }

        // Create user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAccountType((String) session.getAttribute("tempAccountType"));

        // Register
        String result = userService.registerUser(user, questionId, answer);
        if ("SUCCESS".equals(result)) {
            session.removeAttribute("tempAccountType"); 
            request.setAttribute("success", "Registration successful! Awaiting admin approval.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", result);
            loadAndForward(request, response);
        }
    }

    private void loadAndForward(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("questions", securityQuestionService.getAllQuestions());
            request.getRequestDispatcher("/register/step2.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("step1");
        }
    }
}