package com.tss.dao;

import com.tss.database.DBConnection;
import com.tss.model.BankConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankConfigDAO {
    public BankConfig getBankConfig() {
        String sql = "SELECT * FROM bank_config LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BankConfig config = new BankConfig();
                config.setBankName(rs.getString("bank_name"));
                config.setBranchName(rs.getString("branch_name"));
                config.setBranchCode(rs.getString("branch_code"));
                config.setIfscCode(rs.getString("ifsc_code"));
                config.setSupportEmail(rs.getString("support_email"));
                return config;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new BankConfig(); // fallback
    }
}