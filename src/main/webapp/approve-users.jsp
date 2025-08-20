<!-- webapp/admin/approve-users.jsp -->

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.tss.model.User"%>
<%
List<User> pendingUsers = (List<User>) request.getAttribute("pendingUsers");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>Arya Vikas Bank - Approve Users</title>
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
}
</style>
</head>
<body>
	<div class="d-flex">
		<jsp:include page="admin-sidebar.jsp" />

		<div class="flex-grow-1 p-4">
			<h2>
				<i class="fas fa-check-circle"></i> Approve Users
			</h2>
			<hr>

			<%
			if (pendingUsers == null || pendingUsers.isEmpty()) {
			%>
			<div class="alert alert-info">No users pending approval.</div>
			<%
			} else {
			%>
			<table class="table table-bordered table-striped">
				<thead class="table-dark">
					<tr>
						<th>ID</th>
						<th>Username</th>
						<th>Email</th>
						<th>Account Type</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<%
					for (User u : pendingUsers) {
					%>
					<tr>
						<td><%=u.getUserId()%></td>
						<td><%=u.getUsername()%></td>
						<td><%=u.getEmail()%></td>
						<td><%=u.getAccountType()%></td>
						<td>
							<!-- Approve Button -->
							<form action="${pageContext.request.contextPath}/admin/approve" method="post" class="d-inline">
								<input type="hidden" name="userId" value="<%=u.getUserId()%>" />
								<button type="submit" name="action" value="approve"
									class="btn btn-success btn-sm">
									<i class="fas fa-check"></i> Approve
								</button>
							</form>

							<!-- Reject Button -->
							<button type="button" class="btn btn-danger btn-sm"
								data-bs-toggle="modal"
								data-bs-target="#rejectModal<%=u.getUserId()%>">
								<i class="fas fa-times"></i> Reject
							</button>
						</td>
					</tr>

					<!-- Reject Modal -->
					<div class="modal fade" id="rejectModal<%=u.getUserId()%>"
						tabindex="-1">
						<div class="modal-dialog">
							<div class="modal-content">
								<form action="${pageContext.request.contextPath}/admin/approve" method="post">
									<div class="modal-header">
										<h5 class="modal-title">
											Reject User:
											<%=u.getUsername()%>
										</h5>
										<button type="button" class="btn-close"
											data-bs-dismiss="modal"></button>
									</div>
									<div class="modal-body">
										<input type="hidden" name="userId" value="<%=u.getUserId()%>" />
										<input type="hidden" name="action" value="reject" />
										<label for="reason<%=u.getUserId()%>">Reason for Rejection</label>
										<textarea name="reason" id="reason<%=u.getUserId()%>"
											class="form-control" rows="3" required></textarea>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-secondary"
											data-bs-dismiss="modal">Cancel</button>
										<button type="submit" class="btn btn-danger">Reject</button>
									</div>
								</form>
							</div>
						</div>
					</div>

					<%
					}
					%>
				</tbody>
			</table>
			<%
			}
			%>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
