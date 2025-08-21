package com.tss.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.dto.UserFdView;
import com.tss.service.FdService;

@WebServlet("/user/my-fds")
public class MyFDServlet extends HttpServlet {
    private final FdService fdService = new FdService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            session.setAttribute("error", "Please login first.");
            resp.sendRedirect("login");
            return;
        }

        // Get both active FDs and pending applications
        List<UserFdView> fdSummary = fdService.getUserFdSummary(userId);

        req.setAttribute("fdSummary", fdSummary);
        req.getRequestDispatcher("/my-fds.jsp").forward(req, resp);
    }
}