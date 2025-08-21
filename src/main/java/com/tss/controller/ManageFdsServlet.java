// com/tss/servlet/ManageFdsServlet.java
package com.tss.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.dao.FdDAO;
import com.tss.model.FixedDeposit;
import com.tss.service.FdService;

@WebServlet("/admin/manage-fds")
public class ManageFdsServlet extends HttpServlet {
    private final FdService fdService = new FdService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer adminId = (Integer) session.getAttribute("adminId");

        if (adminId == null) {
            session.setAttribute("error", "Please login as admin.");
            resp.sendRedirect("admin-login.jsp");
            return;
        }

        String view = req.getParameter("view");

        switch (view == null ? "" : view) {
            case "approve":
                req.setAttribute("pendingApps", fdService.getPendingApplications());
                req.getRequestDispatcher("/fd-approve.jsp").forward(req, resp);
                break;
            case "close":
                req.setAttribute("activeFds", fdService.getActiveFixedDeposits());
                req.getRequestDispatcher("/fd-close.jsp").forward(req, resp);
                break;
            case "all":
                req.setAttribute("allFds", fdService.getAllFixedDeposits());
                req.getRequestDispatcher("/fd-all.jsp").forward(req, resp);
                break;
            default:
                setupDashboardData(req);
                req.getRequestDispatcher("/fd-dashboard.jsp").forward(req, resp);
                break;
        }
    }

    private void setupDashboardData(HttpServletRequest req) {
        FdDAO dao = new FdDAO();
        Map<String, Integer> counts = new HashMap<>();
        counts.put("PENDING", dao.getPendingApplications().size());

        List<FixedDeposit> allFds = dao.getAllFixedDeposits();
        int active = 0, closed = 0;
        for (FixedDeposit fd : allFds) {
            if ("ACTIVE".equals(fd.getStatus())) active++;
            else if ("CLOSED".equals(fd.getStatus())) closed++;
        }
        counts.put("APPROVED", active + closed);
        counts.put("REJECTED", 0); // Query from fd_applications if needed
        counts.put("CLOSED", closed);

        req.setAttribute("counts", counts);
    }
}