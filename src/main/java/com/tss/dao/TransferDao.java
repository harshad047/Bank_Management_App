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

public class TransferDao {

	public boolean makeTransfer(Transfer transfer) throws Exception {
	    String checkBalance = "SELECT balance FROM accounts WHERE account_id = ?";
	    String deductBalance = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
	    String addBalance = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
	    String insertTransfer = "INSERT INTO transfers (from_account_id, to_account_id, amount, description, transfer_time) VALUES (?, ?, ?, ?, ?)";

	    try (Connection conn = DBConnection.getConnection()) {
	        conn.setAutoCommit(false);

	        try (
	            PreparedStatement psCheck = conn.prepareStatement(checkBalance);
	            PreparedStatement psDeduct = conn.prepareStatement(deductBalance);
	            PreparedStatement psAdd = conn.prepareStatement(addBalance);
	            PreparedStatement psInsert = conn.prepareStatement(insertTransfer)
	        ) {
	            // 1️⃣ Check sender balance
	            psCheck.setInt(1, transfer.getFromAccountId());
	            ResultSet rs = psCheck.executeQuery();
	            if (rs.next()) {
	                BigDecimal currentBalance = rs.getBigDecimal("balance");
	                if (currentBalance.compareTo(transfer.getAmount()) < 0) {
	                    // Insufficient funds
	                    throw new Exception("INSUFFICIENT_BALANCE");
	                }
	            } else {
	                throw new Exception("SENDER_ACCOUNT_NOT_FOUND");
	            }

	            // 2️⃣ Deduct from sender
	            psDeduct.setBigDecimal(1, transfer.getAmount());
	            psDeduct.setInt(2, transfer.getFromAccountId());
	            psDeduct.executeUpdate();

	            // 3️⃣ Add to receiver
	            psAdd.setBigDecimal(1, transfer.getAmount());
	            psAdd.setInt(2, transfer.getToAccountId());
	            psAdd.executeUpdate();

	            // 4️⃣ Record transfer
	            psInsert.setInt(1, transfer.getFromAccountId());
	            psInsert.setInt(2, transfer.getToAccountId());
	            psInsert.setBigDecimal(3, transfer.getAmount());
	            psInsert.setString(4, transfer.getDescription());
	            psInsert.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
	            psInsert.executeUpdate();

	            conn.commit();
	            return true;

	        } catch (Exception e) {
	            conn.rollback();
	            throw e;
	        }
	    }
	}


    public List<Transfer> getTransfersByAccount(int accountId) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT * FROM transfers WHERE from_account_id = ? OR to_account_id = ? ORDER BY transfer_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
        String sql = "SELECT a.account_id, a.account_number, u.username AS accountHolderName, u.account_type " +
                     "FROM accounts a " +
                     "JOIN users u ON a.user_id = u.user_id " +
                     "WHERE u.status='APPROVED' AND a.account_id <> ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, excludeAccountId);  // exclude current user's account
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
