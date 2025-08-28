package com.tss.service;

import com.tss.dao.BeneficiaryDao;
import com.tss.model.Beneficiary;

import java.sql.SQLException;
import java.util.List;

public class BeneficiaryService {
    private final BeneficiaryDao dao = new BeneficiaryDao();

    public boolean addBeneficiary(Beneficiary b) {
        try {
            return dao.addBeneficiary(b);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeBeneficiary(int beneficiaryId, int userId) {
        try {
            return dao.removeBeneficiary(beneficiaryId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Beneficiary> getAllBeneficiaries(int userId) {
        try {
            return dao.getAllBeneficiaries(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
