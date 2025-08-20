// src/main/java/com/tss/controller/RegisterStep1Controller.java

package com.tss.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register/step1")
public class RegisterStep1Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register/step1.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountType = request.getParameter("accountType");
        if (!"SAVINGS".equalsIgnoreCase(accountType) && !"CURRENT".equalsIgnoreCase(accountType)) {
            request.setAttribute("error", "Please select a valid account type.");
            request.getRequestDispatcher("/register/step1.jsp").forward(request, response);
            return;
        }

        // Store in session
        HttpSession session = request.getSession();
        session.setAttribute("tempAccountType", accountType);
        response.sendRedirect("step2");
    }
}