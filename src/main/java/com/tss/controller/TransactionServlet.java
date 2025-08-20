package com.tss.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.model.Account;
import com.tss.model.Transaction;
import com.tss.service.AccountService;
import com.tss.service.TransactionService;

@WebServlet("/user/transaction")
public class TransactionServlet extends HttpServlet {

    private TransactionService txnService;
    private AccountService accountService = new AccountService();

    @Override
    public void init() {
        txnService = new TransactionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Integer userIdObj = (Integer) session.getAttribute("userId");
        int userId = userIdObj;

        // 🔹 Fetch transactions
        List<Transaction> transactions = txnService.getTransactionsByUser(userId);
        req.setAttribute("transactions", transactions);

        // 🔹 Check for success/error params
        String successParam = req.getParameter("success");
        String errorParam = req.getParameter("error");

        if ("1".equals(successParam)) {
            req.setAttribute("success", "Transaction completed successfully!");
        }
        if (errorParam != null) {
            req.setAttribute("error", errorParam);
        }

        req.getRequestDispatcher("/transaction.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Integer userIdObj = (Integer) session.getAttribute("userId");
        Integer accountIdObj = (Integer) session.getAttribute("accountId");

        if (userIdObj == null || accountIdObj == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        int userId = userIdObj;
        int accountId = accountIdObj;

        Account account = accountService.findByUserId(userId);
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/user/transaction?error=Account not found");
            return;
        }

        BigDecimal balance = account.getBalance();

        String action = req.getParameter("action"); // credit or debit
        String amountStr = req.getParameter("amount");
        String description = req.getParameter("description");

        // Basic validation
        if (amountStr == null || amountStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/user/transaction?error=Amount is required");
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/user/transaction?error=Invalid amount");
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            resp.sendRedirect(req.getContextPath() + "/user/transaction?error=Amount must be greater than 0");
            return;
        }

     
        boolean success = false;
        if ("credit".equalsIgnoreCase(action)) {
            success = txnService.credit(userId, accountId, amount, description);
        } else if ("debit".equalsIgnoreCase(action)) {
            // check balance
            if (amount.compareTo(balance) > 0) {
                resp.sendRedirect(req.getContextPath() + "/user/transaction?error=Insufficient balance");
                return;
            }
            success = txnService.debit(userId, accountId, amount, description);
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/transaction?error=Invalid action");
            return;
        }

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/user/transaction?success=1");
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/transaction?error=Transaction failed");
        }
    }
}
