package com.tss.controller;

import com.tss.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet("/user/analysis")
public class AnalysisServlet extends HttpServlet {

    private TransactionService txnService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            resp.sendRedirect("../login.jsp");
            return;
        }

        int accountId = (int) session.getAttribute("accountId");

        String monthParam = req.getParameter("month");      // format: YYYY-MM
        String fromDateParam = req.getParameter("fromDate");
        String toDateParam = req.getParameter("toDate");

        // Fetch credit & debit summaries
        Map<String, BigDecimal> creditData = txnService.getMonthlyCredits(accountId, monthParam, fromDateParam, toDateParam);
        Map<String, BigDecimal> debitData = txnService.getMonthlyDebits(accountId, monthParam, fromDateParam, toDateParam);

        req.setAttribute("creditData", creditData);
        req.setAttribute("debitData", debitData);
        req.getRequestDispatcher("/user-analysis.jsp").forward(req, resp);
    }
}
