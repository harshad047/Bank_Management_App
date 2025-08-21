package com.tss.controller;

import com.tss.model.FdApplication;
import com.tss.service.FdService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/fd-pending")
public class PendingFdServlet extends HttpServlet {
    private final FdService fdService = new FdService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer adminId = (Integer) session.getAttribute("admin_id");

        if (adminId == null) {
            session.setAttribute("error", "Please login as admin.");
            resp.sendRedirect("admin-login.jsp");
            return;
        }

        List<FdApplication> pendingApps = fdService.getPendingApplications();
        req.setAttribute("pendingApps", pendingApps);
        req.getRequestDispatcher("/admin/fd-pending.jsp").forward(req, resp);
    }
}