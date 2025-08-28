package com.tss.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.dao.AccountDAO;
import com.tss.model.Account;
import com.tss.model.Transfer;
import com.tss.service.AccountService;
import com.tss.service.TransferService;

@WebServlet("/user/transfer")
public class TransferServlet extends HttpServlet {

    private TransferService transferService;
    private AccountService accountService;

    @Override
    public void init() {
        transferService = new TransferService();
        accountService = new AccountService();   // ✅ FIX: initialize service
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            resp.sendRedirect("../login.jsp");
            return;
        }

        int accountId = (int) session.getAttribute("accountId");

        AccountDAO accountDao = new AccountDAO();
        try {
            Account account = accountDao.findByUserId(accountId);
            BigDecimal balance = account.getBalance();
            session.setAttribute("balance", balance);
        } catch (SQLException e) {
            System.out.println("[TransferServlet][doGet] Error fetching account balance: " + e.getMessage());
        }

        // ✅ fetch transfers for current account
        List<Transfer> transfers = transferService.getTransfers(accountId);
        req.setAttribute("transfers", transfers);

        // ✅ fetch other accounts for dropdown
        List<Account> accounts = transferService.getActiveAccountsExcluding(accountId);
        req.setAttribute("accounts", accounts);

        // ✅ success/error handling
        String successParam = req.getParameter("success");
        String errorParam = req.getParameter("error");
        if (successParam != null) {
            req.setAttribute("success", "Transfer successful!");
        }
        if ("insufficient".equals(errorParam)) {
            req.setAttribute("error", "Insufficient balance to complete the transfer!");
        } else if (errorParam != null) {
            req.setAttribute("error", "Transfer failed!");
        }

        req.getRequestDispatcher("/transfer.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            resp.sendRedirect("../login.jsp");
            return;
        }

        int fromAccountId = (int) session.getAttribute("accountId");
        int fromUserId = (int) session.getAttribute("userId");

        try {
            int toAccountId = Integer.parseInt(req.getParameter("receiverId"));
            int toUserId = accountService.finduserIdByaccountId(toAccountId);

            BigDecimal amount = new BigDecimal(req.getParameter("amount"));
            String description = req.getParameter("description"); // optional

            // ✅ FIX: Call correct service method
            boolean success = transferService.transfer(fromAccountId, fromUserId, 
                                                       toAccountId, toUserId, 
                                                       amount, description);

            if (success) {
                resp.sendRedirect("transfer?success=1");
            } else {
                resp.sendRedirect("transfer?error=1");
            }

        } catch (NumberFormatException e) {
            resp.sendRedirect("transfer?error=1");
        } catch (Exception ex) {
            if ("INSUFFICIENT_BALANCE".equals(ex.getMessage())) {
                resp.sendRedirect("transfer?error=insufficient");
            } else {
                System.out.println("[TransferServlet][doPost] Error: " + ex.getMessage());
                resp.sendRedirect("transfer?error=1");
            }
        }
    }
}
