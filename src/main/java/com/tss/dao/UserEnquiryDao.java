package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.tss.database.DBConnection;
import com.tss.model.User;
import com.tss.model.UserEnquiry;

public class UserEnquiryDao {

    // Insert new enquiry
    public boolean addEnquiry(UserEnquiry enquiry) {
        String sql = "INSERT INTO user_enquiries (user_id, query_type, description) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, enquiry.getUserId());
            ps.setString(2, enquiry.getQueryType());
            ps.setString(3, enquiry.getDescription());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all enquiries of a user
    public List<UserEnquiry> getEnquiriesByUser(int userId) {
        List<UserEnquiry> list = new ArrayList<>();
        String sql = "SELECT * FROM user_enquiries WHERE user_id = ? ORDER BY submitted_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserEnquiry e = new UserEnquiry();
                e.setEnquiryId(rs.getInt("enquiry_id"));
                e.setUserId(rs.getInt("user_id"));
                e.setQueryType(rs.getString("query_type"));
                e.setDescription(rs.getString("description"));
                e.setStatus(rs.getString("status"));
                e.setSubmittedAt(rs.getTimestamp("submitted_at"));
                e.setResolvedAt(rs.getTimestamp("resolved_at"));
                e.setAdminResponse(rs.getString("admin_response"));
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<UserEnquiry> getAllComplaints() {
        List<UserEnquiry> list = new ArrayList<>();
        String sql = "SELECT ue.*, u.username FROM user_enquiries ue " +
                     "JOIN users u ON ue.user_id = u.user_id ORDER BY ue.submitted_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserEnquiry ue = new UserEnquiry();
                ue.setEnquiryId(rs.getInt("enquiry_id"));
                ue.setUserId(rs.getInt("user_id"));
                ue.setQueryType(rs.getString("query_type"));
                ue.setDescription(rs.getString("description"));
                ue.setStatus(rs.getString("status"));
                ue.setSubmittedAt(rs.getTimestamp("submitted_at"));
                ue.setResolvedAt(rs.getTimestamp("resolved_at"));
                ue.setAdminResponse(rs.getString("admin_response"));

                User user = new User();
                user.setUsername(rs.getString("username"));
                ue.setUser(user);

                list.add(ue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update complaint status and admin response
    public boolean updateComplaint(UserEnquiry enquiry) {
        String sql = "UPDATE user_enquiries SET status = ?, admin_response = ?, resolved_at = ? WHERE enquiry_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, enquiry.getStatus());
            ps.setString(2, enquiry.getAdminResponse());

            if ("RESOLVED".equals(enquiry.getStatus()) || "CLOSED".equals(enquiry.getStatus())) {
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            } else {
                ps.setTimestamp(3, null);
            }

            ps.setInt(4, enquiry.getEnquiryId());

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public UserEnquiry getComplaintsById(int enquiryId) {
        UserEnquiry enquiry = null;
        String sql = "SELECT * FROM user_enquiries WHERE enquiry_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enquiryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                enquiry = new UserEnquiry();
                enquiry.setEnquiryId(rs.getInt("enquiry_id"));
                enquiry.setUserId(rs.getInt("user_id"));
                enquiry.setQueryType(rs.getString("query_type"));
                enquiry.setDescription(rs.getString("description"));
                enquiry.setStatus(rs.getString("status"));
                enquiry.setSubmittedAt(rs.getTimestamp("submitted_at"));
                enquiry.setResolvedAt(rs.getTimestamp("resolved_at"));
                enquiry.setAdminResponse(rs.getString("admin_response"));
                enquiry.setSubmittedAt(rs.getTimestamp("created_at"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return enquiry;
    }
}
