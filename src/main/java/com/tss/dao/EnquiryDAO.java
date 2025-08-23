// src/main/java/com/tss/dao/EnquiryDAO.java
package com.tss.dao;

import com.tss.database.DBConnection;
import com.tss.dto.UserEnquiry;

import java.sql.*;

public class EnquiryDAO {

	public boolean submitEnquiry(UserEnquiry enquiry) {
		String INSERT_QUERY = "INSERT INTO user_enquiries (user_id, query_type, description) VALUES (?, ?, ?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnection.getConnection();
			ps = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, enquiry.getUserId());
			ps.setString(2, enquiry.getQueryType());
			ps.setString(3, enquiry.getDescription());

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						enquiry.setEnquiryId(rs.getInt(1));
					}
				}
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}