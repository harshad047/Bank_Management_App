// src/main/java/com/tss/controller/UserPassbookServlet.java
package com.tss.controller;

import com.tss.model.Account;
import com.tss.model.Transaction;
import com.tss.service.PassbookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/passbook")
public class PassbookServlet extends HttpServlet {

    private PassbookService service = new PassbookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        Account account = service.getAccount(userId);
        List<Transaction> transactions = service.getRecentTransactions(userId, 20); // last 20 txns

        request.setAttribute("account", account);
        request.setAttribute("transactions", transactions);
        request.getRequestDispatcher("/passbook.jsp").forward(request, response);
    }
}
