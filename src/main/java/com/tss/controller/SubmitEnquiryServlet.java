// src/main/java/com/tss/controller/SubmitEnquiryServlet.java
package com.tss.controller;

import com.tss.dto.UserEnquiry;
import com.tss.service.EnquiryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/submitEnquiry")
public class SubmitEnquiryServlet extends HttpServlet {
    private final EnquiryService enquiryService = new EnquiryService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String queryType = request.getParameter("queryType");
        String description = request.getParameter("description");

        UserEnquiry enquiry = new UserEnquiry(userId, queryType, description);
        boolean success = enquiryService.submitEnquiry(enquiry);

        if (success) {
            session.setAttribute("enquiryMsg", "✅ Your enquiry has been submitted successfully.");
        } else {
            session.setAttribute("enquiryMsg", "❌ Failed to submit enquiry. Please try again.");
        }

        // PRG -> Redirect clears form input but keeps other dashboard data intact
        response.sendRedirect("user-dashboard.jsp");
    }
}