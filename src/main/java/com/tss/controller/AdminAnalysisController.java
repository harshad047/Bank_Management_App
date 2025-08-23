// src/main/java/com/tss/controller/AdminAnalysisController.java

package com.tss.controller;

import com.tss.model.MonthlySummary;
import com.tss.service.AccountService;
import com.tss.service.TransactionService;
import com.tss.service.UserService;
import com.tss.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/analysis")
public class AdminAnalysisController extends HttpServlet {

    private final UserService userService = new UserService();
    private final AccountService accountService = new AccountService();
    private final TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Check if admin is logged in
        if (!SessionUtils.isAdmin(request)) {
            response.sendRedirect("../login");
            return;
        }

        try {
            // 📊 KPI Metrics
            List<com.tss.model.User> users = userService.getAllUsers();
            long totalUsers = users.size();
            long approvedCount = users.stream().filter(u -> "APPROVED".equals(u.getStatus())).count();
            long pendingCount = users.stream().filter(u -> "PENDING".equals(u.getStatus())).count();
            long rejectedCount = users.stream().filter(u -> "REJECTED".equals(u.getStatus())).count();
            long newRegistrationsToday = userService.getNewRegistrationsToday();
            BigDecimal totalBalance = accountService.getTotalBalance();
            long totalTransactions = transactionService.getTotalTransactionCount();

            // 📈 Monthly Summary for Chart
            List<MonthlySummary> monthlySummary = transactionService.getMonthlySummaryForAllUsers();
            StringBuilder labels = new StringBuilder();
            StringBuilder credits = new StringBuilder();
            StringBuilder debits = new StringBuilder();

            for (MonthlySummary m : monthlySummary) {
                // wrap labels in quotes for valid JS array
                append(labels, "\"" + m.getLabel() + "\"");
                append(credits, m.getTotalCredit().toString());
                append(debits, m.getTotalDebit().toString());
            }

            // 📤 Set Attributes
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("approvedCount", approvedCount);
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("rejectedCount", rejectedCount);
            request.setAttribute("newRegistrationsToday", newRegistrationsToday);
            request.setAttribute("totalBalance", totalBalance);
            request.setAttribute("totalTransactions", totalTransactions);
            request.setAttribute("monthlySummary", monthlySummary);
            request.setAttribute("chartLabels", labels.toString());
            request.setAttribute("chartCredits", credits.toString());
            request.setAttribute("chartDebits", debits.toString());

            // 🚀 Forward to JSP
            request.getRequestDispatcher("/admin-analysis.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error loading analytics data.");
        }
    }

    private void append(StringBuilder sb, String value) {
        if (sb.length() > 0) sb.append(",");
        sb.append(value);
    }
}
