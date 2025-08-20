<!-- webapp/admin/manage-users.jsp -->

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.tss.model.User"%>
<%
List<User> users = (List<User>) request.getAttribute("users");
String searchQuery = request.getParameter("search") != null ? request.getParameter("search") : "";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>Arya Vikas Bank - Manage Users</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<style>
body {
	background-color: #f4f6f9;
}

.sidebar {
	background: #1a2537;
	min-height: 100vh;
}

.sidebar a {
	color: #dfe4ea;
}

.sidebar a:hover {
	background: #2f3b52;
}

@media print {
	.sidebar, .btn, .modal {
		display: none !important;
	}
}
</style>
</head>
<body>
	<div class="d-flex">

		<!-- Sidebar -->
		<jsp:include page="admin-sidebar.jsp" />

		<!-- Main Content -->
		<div class="flex-grow-1 p-4">
			<h2>
				<i class="fas fa-users"></i> Manage Users
			</h2>
			<hr>

			<!-- Search Bar -->
			<form method="get" class="row mb-4">
				<div class="col-md-8">
					<input type="text" name="search" class="form-control"
						placeholder="Search by username, email, or account number..."
						value="<%=searchQuery%>" />
				</div>
				<div class="col-md-2">
					<button type="submit" class="btn btn-primary w-100">Search</button>
				</div>
				<div class="col-md-2">
					<a href="manage-users" class="btn btn-secondary w-100">Reset</a>
				</div>
			</form>

			<!-- Success/Error Messages -->
			<%
			if (request.getAttribute("success") != null) {
			%>
			<div class="alert alert-success"><%=request.getAttribute("success")%></div>
			<%
			}
			%>
			<%
			if (request.getAttribute("error") != null) {
			%>
			<div class="alert alert-danger"><%=request.getAttribute("error")%></div>
			<%
			}
			%>

			<!-- Users Table -->
			<div class="table-responsive">
				<table class="table table-bordered table-hover align-middle">
					<thead class="table-dark">
						<tr>
							<th>ID</th>
							<th>Username</th>
							<th>Email</th>
							<th>Phone</th>
							<th>Account Type</th>
							<th>Status</th>
							<th class="text-center">Actions</th>
						</tr>
					</thead>
					<tbody>
						<%
						if (users == null || users.isEmpty()) {
						%>
						<tr>
							<td colspan="7" class="text-center text-muted">No users
								found.</td>
						</tr>
						<%
						} else {
						for (User u : users) {
							String status = u.getStatus() != null ? u.getStatus() : "UNKNOWN";
							String badgeClass = "PENDING".equals(status) ? "bg-warning text-dark"
							: "APPROVED".equals(status) ? "bg-success" : "REJECTED".equals(status) ? "bg-danger" : "bg-secondary";
						%>
						<tr>
							<td><%=u.getUserId()%></td>
							<td><%=u.getUsername()%></td>
							<td><%=u.getEmail()%></td>
							<td><%=u.getPhone() != null ? u.getPhone() : "N/A"%></td>
							<td><%=u.getAccountType()%></td>
							<td><span class="badge <%=badgeClass%>"><%=status%></span></td>
							<td class="text-center"><a
								href="view-user?userId=<%=u.getUserId()%>"
								class="btn btn-info btn-sm"> <i class="fas fa-eye"></i>
							</a> <!-- Edit Button (Modal Trigger) -->
								<button type="button" class="btn btn-warning btn-sm"
									data-bs-toggle="modal"
									data-bs-target="#editModal<%=u.getUserId()%>">
									<i class="fas fa-edit"></i>
								</button> <!-- Delete Button -->
								<button type="button" class="btn btn-danger btn-sm"
									data-bs-toggle="modal"
									data-bs-target="#deleteModal<%=u.getUserId()%>">
									<i class="fas fa-trash"></i>
								</button></td>
						</tr>

						<!-- Edit Modal -->
						<div class="modal fade" id="editModal<%=u.getUserId()%>"
							tabindex="-1" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content">
									<form action="manage-users" method="post">
										<div class="modal-header">
											<h5 class="modal-title">
												Edit User Status:
												<%=u.getUsername()%></h5>
											<button type="button" class="btn-close"
												data-bs-dismiss="modal"></button>
										</div>
										<div class="modal-body">
											<input type="hidden" name="userId"
												value="<%=u.getUserId()%>" /> <input type="hidden"
												name="action" value="edit" />
											<div class="mb-3">
												<label class="form-label">New Status</label> <select
													class="form-select" name="newStatus" required>
													<option value="">-- Select Status --</option>
													<%
													if ("REJECTED".equals(status) || "DEACTIVATED".equals(status)) {
													%>
													<option value="APPROVED">Activate</option>
													<%
													} 
													%>
												</select>
											</div>

										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary"
												data-bs-dismiss="modal">Cancel</button>
											<button type="submit" class="btn btn-warning">Update</button>
										</div>
									</form>
								</div>
							</div>
						</div>

						<!-- Delete Modal -->
						<div class="modal fade" id="deleteModal<%=u.getUserId()%>"
							tabindex="-1" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content">
									<form action="manage-users" method="post">
										<div class="modal-header">
											<h5 class="modal-title">
												Delete User:
												<%=u.getUsername()%></h5>
											<button type="button" class="btn-close"
												data-bs-dismiss="modal"></button>
										</div>
										<div class="modal-body">
											<input type="hidden" name="userId"
												value="<%=u.getUserId()%>" /> <input type="hidden"
												name="action" value="delete" />
											<p>Are you sure you want to delete this user? This action
												cannot be undone.</p>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary"
												data-bs-dismiss="modal">Cancel</button>
											<button type="submit" class="btn btn-danger">Delete</button>
										</div>
									</form>
								</div>
							</div>
						</div>
						<%
						}
						}
						%>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
