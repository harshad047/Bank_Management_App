package com.tss.controller;

import com.tss.model.UserEnquiry;
import com.tss.model.User;
import com.tss.service.UserEnquiryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/complaints")
public class UserEnquiryController extends HttpServlet {

    private UserEnquiryService enquiryService = new UserEnquiryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            // User not logged in, redirect to login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Fetch complaints and forward to JSP
        List<UserEnquiry> complaints = enquiryService.getUserComplaints(userId);
        request.setAttribute("complaints", complaints);

        // Correct JSP path
        request.getRequestDispatcher("/user-complaints.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Read form data
        String queryType = request.getParameter("queryType");
        String description = request.getParameter("description");

        UserEnquiry enquiry = new UserEnquiry();
        enquiry.setUserId(userId);
        enquiry.setQueryType(queryType);
        enquiry.setDescription(description);

        // Register complaint
        boolean success = enquiryService.registerComplaint(enquiry);
        if (success) {
            session.setAttribute("successMsg", "Complaint submitted successfully!");
        } else {
            session.setAttribute("errorMsg", "Failed to submit complaint.");
        }

        response.sendRedirect(request.getContextPath() + "/user/complaints");
    }
}
