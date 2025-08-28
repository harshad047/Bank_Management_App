package com.tss.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tss.database.DBConnection;
import com.tss.model.Account;
import com.tss.model.Transfer;
import com.tss.service.TransactionService;

public class TransferDao {

	public boolean makeTransfer(int fromUserId, int fromAccountId, int toUserId, int toAccountId, BigDecimal amount,
			String description) throws Exception {

		BigDecimal currentBalance = null;
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT balance FROM accounts WHERE account_id=?")) {
			ps.setInt(1, fromAccountId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					currentBalance = rs.getBigDecimal("balance");
				} else {
					throw new Exception("SENDER_ACCOUNT_NOT_FOUND");
				}
			}
		}
		
		System.out.println("[TransferDao] Starting transfer: " +
                "fromAccount=" + fromAccountId + 
                ", toAccount=" + toAccountId + 
                ", amount=" + amount);


		if (currentBalance.compareTo(amount) < 0) {
			throw new Exception("INSUFFICIENT_BALANCE");
		}

		try (Connection conn = DBConnection.getConnection()) {
			conn.setAutoCommit(false);

			try {
				TransactionService txnService = new TransactionService();

				String debitDesc = "Transferred to account " + toAccountId;
	            String creditDesc = "Received from account " + fromAccountId;
	            
				System.out.println("[TransferDao] Attempting debit from account: " + fromAccountId);

				boolean debited = txnService.debit(fromUserId, fromAccountId, amount, debitDesc);
				
				System.out.println("[TransferDao] Attempting credit to account: " + toAccountId);

				boolean credited = txnService.credit(toUserId, toAccountId, amount, creditDesc);

				if (!debited || !credited) {
					throw new Exception("TRANSFER_FAILED");
				}
				
				String insertTransfer = "INSERT INTO transfers " +
				        "(from_account_id, to_account_id, amount, description, transfer_time) " +
				        "VALUES (?, ?, ?, ?, ?)";

				boolean transferInserted = false;

				try (Connection connection = DBConnection.getConnection();PreparedStatement psInsert = connection.prepareStatement(insertTransfer)) { 
				    psInsert.setInt(1, fromAccountId);
				    psInsert.setInt(2, toAccountId);
				    psInsert.setBigDecimal(3, amount);
				    psInsert.setString(4, description); // general description for audit
				    psInsert.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
				    System.out.println("[TransferDao] Recording transfer in DB...");

				    int rows = psInsert.executeUpdate();
				    transferInserted = (rows > 0);
				}

				if (debited && credited && transferInserted) {
				    conn.commit();
				    return true;
				} else {
				    conn.rollback();
				    return false;
				}

				
			} catch (Exception e) {
				System.out.println("[TransferDao][ERROR] " + e.getMessage());

				conn.rollback();
				throw e;
			}
		}
	}

	public List<Transfer> getTransfersByAccount(int accountId) {
		List<Transfer> list = new ArrayList<>();
		String sql = "SELECT * FROM transfers WHERE from_account_id = ? OR to_account_id = ? ORDER BY transfer_time DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, accountId);
			ps.setInt(2, accountId);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Transfer t = new Transfer();
				t.setTransferId(rs.getInt("transfer_id"));
				t.setFromAccountId(rs.getInt("from_account_id"));
				t.setToAccountId(rs.getInt("to_account_id"));
				t.setAmount(rs.getBigDecimal("amount"));
				t.setDescription(rs.getString("description"));
				t.setTransferTime(rs.getTimestamp("transfer_time"));
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Account> getAllActiveAccountsExcluding(int excludeAccountId) {
		List<Account> list = new ArrayList<>();
		String sql = "SELECT a.account_id, a.account_number, u.username AS accountHolderName, u.account_type "
				+ "FROM accounts a " + "JOIN users u ON a.user_id = u.user_id "
				+ "WHERE u.status='APPROVED' AND a.account_id <> ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, excludeAccountId); // exclude current user's account
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Account acc = new Account();
				acc.setAccountId(rs.getInt("account_id"));
				acc.setAccountNumber(rs.getString("account_number"));
				acc.setAccountHolderName(rs.getString("accountHolderName"));
				acc.setAccountType(rs.getString("account_type"));
				list.add(acc);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
