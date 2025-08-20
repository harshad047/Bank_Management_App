package com.tss.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tss.dao.AccountDAO;
import com.tss.model.Account;
import com.tss.model.Transfer;
import com.tss.service.TransferService;

@WebServlet("/user/transfer")
public class TransferServlet extends HttpServlet {

	private TransferService transferService;

	@Override
	public void init() {
		transferService = new TransferService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("accountId") == null) {
			resp.sendRedirect("../login.jsp");
			return;
		}

		int accountId = (int) session.getAttribute("accountId");

		// 1️⃣ Fetch current balance
		AccountDAO accountDao = new AccountDAO();
		Account account;
		try {
			account = accountDao.findByUserId(accountId);
			BigDecimal balance = account.getBalance();
			session.setAttribute("balance", balance);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		// store in session for JSP

		// 2️⃣ Fetch transfer history
		List<Transfer> transfers = transferService.getTransfers(accountId);
		req.setAttribute("transfers", transfers);

		// 3️⃣ Fetch active accounts for dropdown excluding current account
		List<Account> accounts = transferService.getActiveAccountsExcluding(accountId);
		req.setAttribute("accounts", accounts);

		// 4️⃣ Handle success/error messages
		String successParam = req.getParameter("success");
		String errorParam = req.getParameter("error");
		if (successParam != null)
			req.setAttribute("success", "Transfer successful!");
		if ("insufficient".equals(errorParam)) {
			req.setAttribute("error", "Insufficient balance to complete the transfer!");
		} else if (errorParam != null) {
			req.setAttribute("error", "Transfer failed!");
		}

		req.getRequestDispatcher("/transfer.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("accountId") == null) {
			resp.sendRedirect("../login.jsp");
			return;
		}

		int fromAccountId = (int) session.getAttribute("accountId");

		try {
			int toAccountId = Integer.parseInt(req.getParameter("receiverId"));
			BigDecimal amount = new BigDecimal(req.getParameter("amount"));
			String description = req.getParameter("description"); // optional

			boolean success = transferService.transfer(fromAccountId, toAccountId, amount, description);

			if (success) {
				resp.sendRedirect("transfer?success=1");
			} else {
				resp.sendRedirect("transfer?error=1");
			}

		} catch (NumberFormatException e) {
			resp.sendRedirect("transfer?error=1");
		} catch (Exception ex) {
			if ("INSUFFICIENT_BALANCE".equals(ex.getMessage())) {
				resp.sendRedirect("transfer?error=insufficient");
			} else {
				resp.sendRedirect("transfer?error=1");
			}
		}
	}
}