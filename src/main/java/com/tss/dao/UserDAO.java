// src/main/java/com/tss/dao/UserDAO.java

package com.tss.dao;

import com.tss.model.User;
import com.tss.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
	
	

	// Check if username or email exists (except self)
	public boolean existsByUsernameOrEmail(String username, String email, Integer excludeUserId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE (username = ? OR email = ?) "
				+ (excludeUserId != null ? " AND user_id != ?" : "");
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, email);
			if (excludeUserId != null) {
				ps.setInt(3, excludeUserId);
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	// Insert or Update user (for PENDING registration)
	public boolean saveOrUpdate(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet generatedKeys = null;
		boolean isUpdate = user.getUserId() != 0;

		String insertSql = "INSERT INTO users (username, password, email, phone, account_type, status, created_at) "
				+ "VALUES (?, ?, ?, ?, ?, 'PENDING', NOW())";
		String updateSql = "UPDATE users SET username = ?, password = ?, email = ?, phone = ?, account_type = ? "
				+ "WHERE user_id = ? AND status IN ('REJECTED', 'DEACTIVATED')";

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			if (isUpdate) {
				ps = conn.prepareStatement(updateSql);
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getPhone());
				ps.setString(5, user.getAccountType());
				ps.setInt(6, user.getUserId());
				int rows = ps.executeUpdate();
				if (rows == 0) {
					conn.rollback();
					return false; // Not found or not in valid status
				}
			} else {
				ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getPhone());
				ps.setString(5, user.getAccountType());
				int affected = ps.executeUpdate();
				if (affected == 0) {
					conn.rollback();
					return false;
				}
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					user.setUserId(generatedKeys.getInt(1));
				} else {
					conn.rollback();
					return false;
				}
			}
			conn.commit();
			return true;

		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
			throw e;
		} finally {
			if (generatedKeys != null)
				generatedKeys.close();
			if (ps != null)
				ps.close();
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
		}
	}

	// Find user by username
	public User findByUsername(String username) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapToUser(rs);
				}
			}
		}
		return null;
	}

	// Find user by email
	public User findByEmail(String email) throws SQLException {
		String sql = "SELECT * FROM users WHERE email = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapToUser(rs);
				}
			}
		}
		return null;
	}

	// Find user by ID
	public User findByUserId(int userId) throws SQLException {
		String sql = "SELECT * FROM users WHERE user_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapToUser(rs);
				}
			}
		}
		return null;
	}

	// Update approved user (status, approved_by, approved_at)
	public boolean updateApprovedUser(User user) throws SQLException {
		String sql = "UPDATE users SET status = ?, approved_by = ?, approved_at = ? WHERE user_id = ?";
		try (Connection conn = DBConnection.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, user.getStatus());
				ps.setInt(2, user.getApprovedBy());
				ps.setTimestamp(3, new Timestamp(user.getApprovedAt().getTime()));
				ps.setInt(4, user.getUserId());
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

	// List all users (for admin)
	public List<User> getAllUsers() throws SQLException {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM users ORDER BY created_at DESC";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				users.add(mapToUser(rs));
			}
		}
		return users;
	}

	// Map ResultSet to User
	private User mapToUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setUserId(rs.getInt("user_id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setEmail(rs.getString("email"));
		user.setPhone(rs.getString("phone"));
		user.setAccountType(rs.getString("account_type"));
		user.setStatus(rs.getString("status"));
		user.setApprovedBy(rs.getInt("approved_by"));
		user.setApprovedAt(rs.getTimestamp("approved_at"));
		user.setRejectionReason(rs.getString("rejection_reason"));
		user.setCreatedAt(rs.getTimestamp("created_at"));
		user.setUpdatedAt(rs.getTimestamp("updated_at"));
		return user;
	}

	public List<User> searchUsers(String query) throws SQLException {
		String sql = "SELECT * FROM users WHERE " + "username LIKE ? OR email LIKE ? OR phone LIKE ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			String likeQuery = "%" + query + "%";
			ps.setString(1, likeQuery);
			ps.setString(2, likeQuery);
			ps.setString(3, likeQuery);
			try (ResultSet rs = ps.executeQuery()) {
				List<User> users = new ArrayList<>();
				while (rs.next()) {
					users.add(mapToUser(rs));
				}
				return users;
			}
		}
	}

	public boolean deactivateUser(int userId, int adminId) throws SQLException {
		String sql = "UPDATE users SET status = 'DEACTIVATED', updated_at = NOW() WHERE user_id = ? AND status IN ('APPROVED', 'PENDING', 'REJECTED')";
		try (Connection conn = DBConnection.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, userId);
				int rows = ps.executeUpdate();
				if (rows == 0) {
					conn.rollback();
					return false;
				}

				// Optional: Log in admin_approvals table
				String logSql = "INSERT INTO admin_approvals (user_id, admin_id, action, reason, created_at) VALUES (?, ?, 'DEACTIVATED', 'User deactivated by admin', NOW())";
				try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
					logPs.setInt(1, userId);
					logPs.setInt(2, adminId);
					if (logPs.executeUpdate() == 0) {
						conn.rollback();
						return false;
					}
				}

				conn.commit();
				return true;
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		}
	}

	// com.tss.dao.UserDAO.java

	public long getNewRegistrationsToday() throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE DATE(created_at) = CURDATE()";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getLong(1);
			}
			return 0;
		}
	}

	public boolean updateUser(User user) {
		StringBuilder sql = new StringBuilder("UPDATE users SET email=?, phone=?");
		if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
			sql.append(", password=?");
		}
		sql.append(" WHERE user_id=?");

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql.toString())) {

			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPhone());

			int paramIndex = 3;
			if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
				ps.setString(paramIndex++, user.getPassword());
			}

			ps.setInt(paramIndex, user.getUserId());

			int updatedRows = ps.executeUpdate();
			return updatedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isUserInactive(int userId) {
	    String sql = "SELECT status FROM users WHERE user_id = ?";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, userId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("status").equalsIgnoreCase("deactivated") || rs.getString("status").equalsIgnoreCase("rejected"); // 0 = inactive/deactivated
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // default: assume active if error
	}
	
	public boolean updateUserStatus(int userId, String newStatus, int adminId) throws SQLException {
	    String sql = "UPDATE users SET status = ? WHERE user_id = ?";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, newStatus);
	        ps.setInt(2, userId);

	        int rows = ps.executeUpdate();

	        // ✅ Insert audit trail into admin_approvals table
	        if (rows > 0) {
	            String approvalSql = "INSERT INTO admin_approvals (user_id, admin_id, action, reason) VALUES (?, ?, ?, ?)";
	            try (PreparedStatement ps2 = conn.prepareStatement(approvalSql)) {
	                ps2.setInt(1, userId);
	                ps2.setInt(2, adminId);

	                // 🔹 action ENUM only accepts APPROVED / REJECTED
	                if ("ACTIVE".equalsIgnoreCase(newStatus)) {
	                    ps2.setString(3, "APPROVED");
	                } else if ("REJECTED".equalsIgnoreCase(newStatus)) {
	                    ps2.setString(3, "REJECTED");
	                } else {
	                    ps2.setString(3, "APPROVED"); // default for DEACTIVATED / other statuses
	                }

	                ps2.setString(4, "Status updated to " + newStatus + " by admin");
	                ps2.executeUpdate();
	            }
	        }

	        return rows > 0;
	    }
	}
	
	 public int getTotalCustomers() throws SQLException {
	        String sql = "SELECT COUNT(*) FROM users";
	        try (Connection conn = DBConnection.getConnection();
	             Statement st = conn.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {
	            if (rs.next()) return rs.getInt(1);
	        }
	        return 0;
	    }

	    // New Customers Today
	    public int getNewCustomersToday() throws SQLException {
	        String sql = "SELECT COUNT(*) FROM users WHERE DATE(created_at) = CURDATE()";
	        try (Connection conn = DBConnection.getConnection();
	             Statement st = conn.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {
	            if (rs.next()) return rs.getInt(1);
	        }
	        return 0;
	    }

}
