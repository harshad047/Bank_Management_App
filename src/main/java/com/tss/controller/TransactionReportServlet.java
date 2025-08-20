// com.tss.controller.TransactionReportServlet.java
package com.tss.controller;

import com.tss.model.Transaction;
import com.tss.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/admin/transaction-report")
public class TransactionReportServlet extends HttpServlet {
    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Filters
            String minStr = req.getParameter("minAmount");
            String maxStr = req.getParameter("maxAmount");
            String fromStr = req.getParameter("fromDate");
            String toStr   = req.getParameter("toDate");
            String accountType = req.getParameter("accountType");

            BigDecimal min = (minStr != null && !minStr.isEmpty()) ? new BigDecimal(minStr) : null;
            BigDecimal max = (maxStr != null && !maxStr.isEmpty()) ? new BigDecimal(maxStr) : null;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date from = (fromStr != null && !fromStr.isEmpty()) ? sdf.parse(fromStr) : null;
            Date to   = (toStr != null && !toStr.isEmpty()) ? sdf.parse(toStr) : null;

            List<Transaction> transactions =
                    transactionService.getTransactionsWithFilters(min, max, from, to, accountType);

            req.setAttribute("transactions", transactions);
            req.getRequestDispatcher("/transactionReport.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading transactions");
        }
    }
}
