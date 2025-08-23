package com.tss.service;

import java.util.List;

import com.tss.dao.FdDAO;
import com.tss.dto.UserFdView;
import com.tss.model.FdApplication;
import com.tss.model.FixedDeposit;

public class FdService {
    private final FdDAO fdDAO = new FdDAO();

    public void applyForFd(FdApplication app) {
        fdDAO.createFdApplication(app);
    }

    public List<FdApplication> getPendingApplications() {
        return fdDAO.getPendingApplications();
    }

//    public List<FdApplication> getMyApplications(int userId) {
//        return fdDAO.getApplicationsByUser(userId);
//    }

    public boolean approveFd(int fdAppId, int adminId) {
        return fdDAO.approveFd(fdAppId, adminId);
    }

    public boolean rejectFd(int fdAppId, String reason) {
        return fdDAO.rejectFd(fdAppId, reason);
    }

    public FdApplication getApplicationById(int fdAppId) {
        return fdDAO.getApplicationById(fdAppId);
    }

//    public List<FixedDeposit> getMyFixedDeposits(int userId) {
//        return fdDAO.getFixedDepositsByUser(userId);
//    }
    
    public List<UserFdView> getUserFdSummary(int userId) {
        return fdDAO.getUserFdSummary(userId);
    }

    public boolean updateStatusOfFD(int fdId) {
        return fdDAO.updateStatusFixedDeposit(fdId);
    }
    
    public List<FixedDeposit> getActiveFixedDeposits() {
        return fdDAO.getActiveFixedDeposits();
    }

	public List<FixedDeposit> getAllFixedDeposits()
	{
		return fdDAO.getAllFixedDeposits();
	}

	public FixedDeposit getFixedDepositById(int fdId) {
		return fdDAO.getFixedDepositById(fdId);
	}

	public boolean closeFixedDeposit(int fdId) {
		return fdDAO.closeFixedDeposit(fdId);
	}
	
	
}