package com.tss.dao;

import com.tss.database.DBConnection;
import java.sql.*;

public class UserSecurityAnswerDAO {

    public boolean saveAnswer(int userId, int questionId, String answer) throws SQLException {
        String sql = "INSERT INTO user_security_answers (user_id, question_id, answer) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE answer = VALUES(answer)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setInt(2, questionId);
                ps.setString(3, answer);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public boolean verifyAnswer(int userId, int questionId, String answer) throws SQLException {
        String sql = "SELECT answer FROM user_security_answers WHERE user_id = ? AND question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("answer").equalsIgnoreCase(answer.trim());
                }
            }
        }
        return false;
    }
}