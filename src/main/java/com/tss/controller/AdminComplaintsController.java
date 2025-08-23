package com.tss.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.tss.model.UserEnquiry;
import com.tss.service.AdminApprovalService;
import com.tss.service.UserEnquiryService;

@WebServlet("/admin/complaints")
public class AdminComplaintsController extends HttpServlet {

    private UserEnquiryService enquiryService = new UserEnquiryService();
    private AdminApprovalService approvalService = new AdminApprovalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Fetch all complaints
        List<UserEnquiry> complaints = enquiryService.getAllComplaints();
        request.setAttribute("complaints", complaints);

        // Forward to JSP (can be inside WEB-INF)
        request.getRequestDispatcher("/admin-complaints.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer adminId = (Integer) session.getAttribute("adminId");

        String enquiryIdStr = request.getParameter("enquiryId");
        String status = request.getParameter("status");
        String adminResponse = request.getParameter("adminResponse");

        if (enquiryIdStr == null || status == null) {
            session.setAttribute("errorMsg", "Invalid request.");
            response.sendRedirect(request.getContextPath() + "/admin/complaints");
            return;
        }

        try {
            int enquiryId = Integer.parseInt(enquiryIdStr);
            UserEnquiry enquiry = enquiryService.getComplaintById(enquiryId);

            if (enquiry == null) {
                session.setAttribute("errorMsg", "Complaint not found.");
            } else if ("CLOSED".equals(enquiry.getStatus())) {
                session.setAttribute("errorMsg", "Cannot update a closed complaint.");
            } else {
                // Update status & response
                enquiry.setStatus(status);
                enquiry.setAdminResponse(adminResponse);

                // Set resolvedAt if status is RESOLVED or CLOSED
                if ("RESOLVED".equals(status) || "CLOSED".equals(status)) {
                    enquiry.setResolvedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                }

                boolean success = enquiryService.updateComplaint(enquiry);

                if (success) {
                    // Log admin approval/action
                    String logMessage = "Complaint status updated to " + status;
                    approvalService.logApproval(enquiry.getUserId(), adminId, status, logMessage);
                    session.setAttribute("successMsg", "Complaint updated successfully.");
                } else {
                    session.setAttribute("errorMsg", "Failed to update complaint.");
                }
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMsg", "Invalid complaint ID.");
        }

        // Redirect back to refresh the list
        response.sendRedirect(request.getContextPath() + "/admin/complaints");
    }
}
