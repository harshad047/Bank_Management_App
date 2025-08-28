// com/tss/controller/ApproveFdServlet.java

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
import com.tss.service.AdminApprovalService;
import com.tss.service.FdService;
import com.tss.service.TransactionService;

@WebServlet("/approve-fd")
public class ApproveFdServlet extends HttpServlet {
    private final FdService fdService = new FdService();
    private final AdminApprovalService approvalService = new AdminApprovalService();
    private final TransactionService transactionService = new TransactionService();
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer adminId = (Integer) session.getAttribute("adminId"); // ✅ Use correct key: admin_id

        if (adminId == null) {
            session.setAttribute("error", "Please login as admin.");
            resp.sendRedirect("login.jsp");
            return;
        }

        int fdAppId = getInt(req, "fdAppId");
        String action = req.getParameter("action");

        if (fdAppId <= 0 || !(action.equals("approve") || action.equals("reject"))) {
            session.setAttribute("error", "Invalid request.");
            resp.sendRedirect("fd-dashboard?view=approve");
            return;
        }

        FdApplication app = fdService.getApplicationById(fdAppId);
        if (app == null) {
            session.setAttribute("error", "FD application not found.");
            resp.sendRedirect("fd-dashboard?view=approve");
            return;
        }

        
        
        // ✅ 2. Now you have userId
        int userId = app.getUserId();

        // ✅ 3. Perform approve/reject
        boolean success = "approve".equals(action)
                ? fdService.approveFd(fdAppId, adminId)
                : fdService.rejectFd(fdAppId, "Rejected by admin");

        if (success) {
            String status = action.equalsIgnoreCase("approve") ? "APPROVED" : "REJECTED";
            String remarks = "FD Request " + status;
            
          
            if ("approved".equalsIgnoreCase(status)) {
                BigDecimal amount = null;

                // Safely extract amount
                Object amtObj = app.getAmount();
                if (amtObj instanceof BigDecimal) {
                    amount = (BigDecimal) amtObj;
                } else if (amtObj instanceof Number) {
                    amount = BigDecimal.valueOf(((Number) amtObj).doubleValue());
                }

                if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    session.setAttribute("error", "Invalid FD amount.");
                    resp.sendRedirect(req.getContextPath() + "/admin/fd-requests");
                    return;
                }
                
                Account account = accountService.findByUserId(userId);
                
                if(account == null)
                {
                	session.setAttribute("error", "Account Not Found");
                    resp.sendRedirect(req.getContextPath() + "/admin/fd-requests");
                    return;
                }
                
                String description = "FD Approved Of Amount"+amount;
                
                transactionService.debit(userId,account.getAccountId() , amount, description);
            }

            approvalService.logApproval(userId, adminId, status, remarks);

            session.setAttribute("message", "✅ FD application " + action + "d successfully!");
        } else {
            session.setAttribute("error", "❌ Failed to process FD application.");
        }

        resp.sendRedirect(req.getContextPath() + "/fd-approve.jsp");
    }

    // Utility method to safely parse int
    private int getInt(HttpServletRequest req, String param) {
        try {
            return Integer.parseInt(req.getParameter(param));
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }
}