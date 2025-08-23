// src/main/java/com/tss/service/EnquiryService.java
package com.tss.service;

import com.tss.dao.EnquiryDAO;
import com.tss.dto.UserEnquiry;

public class EnquiryService {

    private EnquiryDAO enquiryDAO = new EnquiryDAO();

    public boolean submitEnquiry(UserEnquiry enquiry) {
        if (enquiry.getQueryType() == null || enquiry.getDescription() == null ||
            enquiry.getDescription().trim().length() < 5) {
            return false;
        }
        return enquiryDAO.submitEnquiry(enquiry);
    }
}