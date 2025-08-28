package com.tss.controller;

import com.tss.dao.AccountDAO;
import com.tss.model.Account;
import com.tss.model.Beneficiary;
import com.tss.model.Transfer;
import com.tss.service.BeneficiaryService;
import com.tss.service.TransferService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/user/beneficiaries")
public class BeneficiaryServlet extends HttpServlet {
    private final BeneficiaryService service = new BeneficiaryService();

    private TransferService transferService;

	@Override
	public void init() {
		transferService = new TransferService();
	}
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        
		int accountId = (int) session.getAttribute("accountId");

		
		AccountDAO accountDao = new AccountDAO();
		Account account;
		try {
			account = accountDao.findByUserId(accountId);
			BigDecimal balance = account.getBalance();
			session.setAttribute("balance", balance);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		List<Account> accounts = transferService.getActiveAccountsExcluding(accountId);
		req.setAttribute("accounts", accounts);
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        List<Beneficiary> list = service.getAllBeneficiaries(userId);
        req.setAttribute("beneficiaries", list);
        
        req.getRequestDispatcher("/beneficiaries.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            int accountId = Integer.parseInt(req.getParameter("accountId"));
            int beneficiaryAccId = Integer.parseInt(req.getParameter("beneficiaryAccId"));
            String nickname = req.getParameter("nickname");

            Beneficiary b = new Beneficiary();
            b.setUserId(userId);
            b.setAccountId(accountId);
            b.setBeneficiaryAccId(beneficiaryAccId);
            b.setNickname(nickname);

            service.addBeneficiary(b);
        } else if ("remove".equals(action)) {
            int beneficiaryId = Integer.parseInt(req.getParameter("beneficiaryId"));
            service.removeBeneficiary(beneficiaryId, userId);
        }

        resp.sendRedirect(req.getContextPath() + "/beneficiaries");
    }
}
