package com.tss.controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.model.Account;
import com.tss.model.FdApplication;
import com.tss.service.AccountService;
import com.tss.service.FdService;

@WebServlet("/user/apply-fd")
public class ApplyFdServlet extends HttpServlet {
    private final FdService fdService = new FdService();
    private final AccountService accountService = new AccountService();

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
		BigDecimal balance;
        int tenureMonths;

        try {
        	Account account = accountService.findByUserId(userId);
        	
        	if(account == null)
        	{
        		session.setAttribute("error", "No Account Found");
                resp.sendRedirect(req.getContextPath() + "/user/apply-fd");
                return;
        	}
        	
        	balance =  account.getBalance();
        	
            amount = Double.parseDouble(req.getParameter("amount"));

        	if (BigDecimal.valueOf(amount).compareTo(balance) > 0) {
                session.setAttribute("error", "Insufficient balance.");
                resp.sendRedirect(req.getContextPath() + "/user/apply-fd");
                return;
            }        	
        	
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