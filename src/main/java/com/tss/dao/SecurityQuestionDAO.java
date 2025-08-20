// src/main/java/com/tss/dao/SecurityQuestionDAO.java

package com.tss.dao;

import com.tss.model.SecurityQuestion;
import com.tss.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecurityQuestionDAO {

    public List<SecurityQuestion> getAllQuestions() throws SQLException {
        List<SecurityQuestion> questions = new ArrayList<>();
        String sql = "SELECT * FROM security_questions ORDER BY question_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SecurityQuestion q = new SecurityQuestion();
                q.setQuestionId(rs.getInt("question_id"));
                q.setQuestionText(rs.getString("question_text"));
                questions.add(q);
            }
        }
        return questions;
    }

    public SecurityQuestion findById(int questionId) throws SQLException {
        String sql = "SELECT * FROM security_questions WHERE question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SecurityQuestion q = new SecurityQuestion();
                    q.setQuestionId(rs.getInt("question_id"));
                    q.setQuestionText(rs.getString("question_text"));
                    return q;
                }
            }
        }
        return null;
    }
}