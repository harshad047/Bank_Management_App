// src/main/java/com/tss/service/SecurityQuestionService.java

package com.tss.service;

import com.tss.dao.SecurityQuestionDAO;
import com.tss.model.SecurityQuestion;

import java.sql.SQLException;
import java.util.List;

public class SecurityQuestionService {

    private SecurityQuestionDAO questionDAO = new SecurityQuestionDAO();

    // Get all security questions
    public List<SecurityQuestion> getAllQuestions() {
        try {
            return questionDAO.getAllQuestions();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get question by ID
    public SecurityQuestion getQuestionById(int questionId) {
        try {
            return questionDAO.findById(questionId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}