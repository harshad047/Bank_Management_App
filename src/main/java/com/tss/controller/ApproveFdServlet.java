// com/tss/controller/ApproveFdServlet.java

package com.tss.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import com.tss.model.FdApplication;
import com.tss.service.AdminApprovalService;
import com.tss.service.FdService;

@WebServlet("/approve-fd")
public class ApproveFdServlet extends HttpServlet {
    private final FdService fdService = new FdService();
    private final AdminApprovalService approvalService = new AdminApprovalService();

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

        // ✅ 1. Get FD Application to retrieve userId
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

            approvalService.logApproval(userId, adminId, status, remarks);

            session.setAttribute("message", "✅ FD application " + action + "d successfully!");
        } else {
            session.setAttribute("error", "❌ Failed to process FD application.");
        }

        // ✅ 5. Redirect back to approve view
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