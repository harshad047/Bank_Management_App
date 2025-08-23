package com.tss.service;

import com.tss.dao.UserEnquiryDao;
import com.tss.model.UserEnquiry;

import java.util.List;

public class UserEnquiryService {

    private UserEnquiryDao enquiryDao = new UserEnquiryDao();

    // Register new complaint
    public boolean registerComplaint(UserEnquiry enquiry) {
        return enquiryDao.addEnquiry(enquiry);
    }

    // Get user's complaints
    public List<UserEnquiry> getUserComplaints(int userId) {
        return enquiryDao.getEnquiriesByUser(userId);
    }
    
    public List<UserEnquiry> getAllComplaints() {
        return enquiryDao.getAllComplaints();
    }

    // Admin: update complaint
    public boolean updateComplaint(UserEnquiry enquiry) {
        return enquiryDao.updateComplaint(enquiry);
    }

	public UserEnquiry getComplaintById(int enquiryId) {
		return enquiryDao.getComplaintsById(enquiryId);
	}
}
