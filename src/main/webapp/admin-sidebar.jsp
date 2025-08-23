<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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

.card {
	border-radius: 12px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>

<nav class="sidebar text-white p-3">
	<h5>
		<i class="fas fa-university"></i> Arya Vikas Bank
	</h5>
	<hr>
	<ul class="nav flex-column">
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/admin/dashboard"
			class="nav-link"> <i class="fas fa-tachometer-alt"></i> Dashboard
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/admin/manage-users"
			class="nav-link"> <i class="fas fa-users"></i> Manage Users
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/admin/approve-users"
			class="nav-link"> <i class="fas fa-check-circle"></i> Approve
				Users
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/admin/complaints"
			class="nav-link"> <i class="fas fa-comment-alt"></i> Complaints
		</a></li>
		<!-- 🔹 NEW: Manage FDs -->
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/admin/manage-fds"
			class="nav-link"> <i class="fas fa-certificate"></i> Manage FDs
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/admin/transaction-report"
			class="nav-link"> <i class="fas fa-file-invoice"></i> Reports
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/admin/analysis"
			class="nav-link"> <i class="fas fa-chart-bar"></i> Analytics
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/logout"
			class="nav-link text-danger"> <i class="fas fa-sign-out-alt"></i>
				Logout
		</a></li>
	</ul>
</nav>