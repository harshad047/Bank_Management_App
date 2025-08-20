package com.tss.controller;

import com.tss.dao.TransactionDAO;
import com.tss.model.Transaction;
import com.tss.util.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/user/export-transactions.csv")
public class ExportCsvServlet extends HttpServlet {
    private TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = SessionUtils.getUserId(req);
        if (userId <= 0) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition", "attachment; filename=transactions.csv");
        try (PrintWriter writer = resp.getWriter()) {
            writer.println("txn_id,account_id,user_id,txn_type,amount,description,txn_time,balance_after,channel");
            List<Transaction> txns = transactionDAO.getTransactionsByUserId(userId);
            for (Transaction t : txns) {
                writer.printf("%d,%d,%d,%s,%s,%s,%s,%s,%s\n",
                        t.getTxnId(),
                        t.getAccountId(),
                        t.getUserId(),
                        safe(t.getTxnType()),
                        t.getAmount(),
                        escapeCsv(t.getDescription()),
                        t.getTxnTime(),
                        t.getBalanceAfter(),
                        safe(t.getChannel())
                );
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private String safe(String s) { return s == null ? "" : s; }
    private String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return '"' + s.replace("\"", "\"\"") + '"';
        }
        return s;
    }
}

