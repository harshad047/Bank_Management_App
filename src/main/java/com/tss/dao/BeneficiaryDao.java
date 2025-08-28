package com.tss.dao;

import com.tss.database.DBConnection;
import com.tss.model.Beneficiary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeneficiaryDao {

    public boolean addBeneficiary(Beneficiary b) throws SQLException {
        String sql = "INSERT INTO beneficiaries (user_id, account_id, beneficiary_acc, nickname, is_active) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.getUserId());
            ps.setInt(2, b.getAccountId());
            ps.setInt(3, b.getBeneficiaryAccId());
            ps.setString(4, b.getNickname());
            ps.setBoolean(5, true);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean removeBeneficiary(int beneficiaryId, int userId) throws SQLException {
        String sql = "DELETE FROM beneficiaries WHERE beneficiary_id=? AND user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, beneficiaryId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Beneficiary> getAllBeneficiaries(int userId) throws SQLException {
        List<Beneficiary> list = new ArrayList<>();
        String sql = "SELECT * FROM beneficiaries WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Beneficiary b = new Beneficiary();
                b.setBeneficiaryId(rs.getInt("beneficiary_id"));
                b.setUserId(rs.getInt("user_id"));
                b.setAccountId(rs.getInt("account_id"));
                b.setBeneficiaryAccId(rs.getInt("beneficiary_acc"));
                b.setNickname(rs.getString("nickname"));
                b.setActive(rs.getBoolean("is_active"));
                list.add(b);
            }
        }
        return list;
    }
}
