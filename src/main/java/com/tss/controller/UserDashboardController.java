// src/main/java/com/tss/controller/UserDashboardController.java

package com.tss.controller;

import com.tss.model.User;
import com.tss.service.AccountService;
import com.tss.service.TransactionService;
import com.tss.service.UserService;
import com.tss.util.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/*")
public class UserDashboardController extends HttpServlet {

    private UserService userService = new UserService();
    private AccountService accountService = new AccountService();
    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtils.isUser(request)) {
            response.sendRedirect("../login");
            return;
        }

        String path = request.getPathInfo();

        // Get user from session
        int userId = SessionUtils.getUserId(request);
        User user = userService.findByUserId(userId);

        if (user == null) {
            response.sendRedirect("../login");
            return;
        }

        switch (path == null ? "/" : path) {
        case "/":
        case "/dashboard":
            request.setAttribute("user", user);
            request.setAttribute("account", accountService.findByUserId(userId));

            // Get last month transactions
            try {
                double credited = transactionService.getTotalByTypeLastMonth(userId, "CREDIT");
                double debited = transactionService.getTotalByTypeLastMonth(userId, "DEBIT");
                request.setAttribute("credited", credited);
                request.setAttribute("debited", debited);
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("credited", 0.0);
                request.setAttribute("debited", 0.0);
            }

            request.getRequestDispatcher("/user-dashboard.jsp").forward(request, response);
            break;


            case "/passbook":
			try {
				request.setAttribute("transactions", transactionService.getTransactionsByUserId(userId));
			} catch (SQLException e) {
				e.printStackTrace();
			}
                request.getRequestDispatcher("/user/passbook.jsp").forward(request, response);
                break;

            case "/analysis":
                request.getRequestDispatcher("/user/analysis.jsp").forward(request, response);
                break;

            case "/profile":
                request.setAttribute("user", user);
                request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
                break;

            default:
                response.sendError(404, "Page not found");
        }
    }
}