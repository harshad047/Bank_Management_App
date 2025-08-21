package com.tss.controller;

import com.tss.model.FdApplication;
import com.tss.service.FdService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/apply-fd")
public class ApplyFdServlet extends HttpServlet {
    private final FdService fdService = new FdService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            session.setAttribute("error", "Please login first.");
            resp.sendRedirect("login");
            return;
        }

        // Display any message (success/error)
        req.getRequestDispatcher("/apply-fd.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");        // ✅ Fixed
        Integer accountId = (Integer) session.getAttribute("accountId");  // ✅ Fixed

        if (userId == null || accountId == null) {
            session.setAttribute("error", "Session expired. Please login again.");
            resp.sendRedirect("login");
            return;
        }

        double amount;
        int tenureMonths;

        try {
            amount = Double.parseDouble(req.getParameter("amount"));
            tenureMonths = Integer.parseInt(req.getParameter("tenureMonths"));
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid amount or tenure.");
            resp.sendRedirect(req.getContextPath() + "/user/apply-fd");
            return;
        }

        if (amount < 1000) {
            session.setAttribute("error", "Minimum FD amount is ₹1,000.");
            resp.sendRedirect(req.getContextPath() + "/user/apply-fd");
            return;
        }

        double interestRate = getRateForTenure(tenureMonths);

        FdApplication app = new FdApplication();
        app.setUserId(userId);
        app.setAccountId(accountId);
        app.setAmount(amount);
        app.setTenureMonths(tenureMonths);
        app.setInterestRate(interestRate);

        fdService.applyForFd(app);

        // ✅ Set success message in session
        session.setAttribute("message", "✅ FD application submitted successfully!");

        // Redirect back to the form
        resp.sendRedirect(req.getContextPath() + "/user/apply-fd");
    }

    private double getRateForTenure(int months) {
        return switch (months) {
            case 6 -> 6.5;
            case 12 -> 6.8;
            case 24 -> 7.0;
            case 36 -> 7.1;
            case 60 -> 7.5;
            default -> 6.5;
        };
    }
}