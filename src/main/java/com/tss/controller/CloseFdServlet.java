// com/tss/controller/CloseFdServlet.java

package com.tss.controller;

import com.tss.model.FixedDeposit;
import com.tss.service.AdminApprovalService;
import com.tss.service.FdService;
import com.tss.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/close-fd")
public class CloseFdServlet extends HttpServlet {
    private final FdService fdService = new FdService();
    private final TransactionService transactionService = new TransactionService();
    private final AdminApprovalService approvalService = new AdminApprovalService();

    // ✅ Handle GET request (from <a href="...">)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer adminId = (Integer) session.getAttribute("adminId");  // ✅ correct key
        int fdAppId = getInt(req, "fdAppId");

        if (adminId == null) {
            session.setAttribute("error", "Please login as admin.");
            resp.sendRedirect("login.jsp");
            return;
        }

        int fdId = getInt(req, "fdId");
        
        if (fdId <= 0) {
            session.setAttribute("error", "Invalid FD ID.");
            resp.sendRedirect("fd-dashboard?view=close");
            return;
        }

        // Get FD
        FixedDeposit fd = fdService.getFixedDepositById(fdId);
        fdService.updateStatusOfFD(fdAppId);
        if (fd == null || !"ACTIVE".equals(fd.getStatus())) {
            session.setAttribute("error", "FD not found or already closed.");
            resp.sendRedirect("fd-dashboard?view=close");
            return;
        }

        // Close FD
        boolean closed = fdService.closeFixedDeposit(fdId);
        
        if (!closed) {
            session.setAttribute("error", "Failed to close FD.");
            resp.sendRedirect("fd-dashboard?view=close");
            return;
        }

        // Credit account
        BigDecimal amount = BigDecimal.valueOf(fd.getMaturityAmount());
        boolean credited = transactionService.credit(
            fd.getUserId(),
            fd.getAccountId(),
            amount,
            "FD Closed: FD#" + fd.getFdId()
        );

        if (credited) {
        	approvalService.logApproval(fd.getUserId(), adminId, "CLOSED" ,"Fd Closed By Admin");
            session.setAttribute("message", "✅ FD closed! ₹" + amount + " credited.");
        } else {
            session.setAttribute("error", "FD closed, but credit failed.");
        }

        // Redirect back
        resp.sendRedirect(req.getContextPath() + "/fd-close.jsp");
    }

    private int getInt(HttpServletRequest req, String param) {
        try {
            return Integer.parseInt(req.getParameter(param));
        } catch (Exception e) {
            return -1;
        }
    }
}