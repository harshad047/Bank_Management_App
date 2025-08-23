<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<nav class="sidebar text-white p-3">
	<h5>
		<i class="fas fa-university"></i> Arya Vikas Bank
	</h5>
	<hr>
	<ul class="nav flex-column">
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user/dashboard"
			class="nav-link"> <i class="fas fa-tachometer-alt"></i> Dashboard
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user/passbook"
			class="nav-link"> <i class="fas fa-book"></i> Passbook
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user/transaction"
			class="nav-link"> <i class="fas fa-exchange-alt"></i>
				Transactions
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user/transfer"
			class="nav-link"> <i class="fas fa-money-check-alt"></i> Transfer
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user/complaints"
			class="nav-link"> <i class="fas fa-comment-alt"></i> Complaints
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user/analysis"
			class="nav-link"> <i class="fas fa-chart-line"></i> Monthly
				Analysis
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user/apply-fd"
			class="nav-link"> <i class="fas fa-certificate"></i> Apply FD
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user/my-fds"
			class="nav-link"> <i class="fas fa-box"></i> My FDs
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/user-profile"
			class="nav-link"> <i class="fas fa-user-cog"></i> Profile
		</a></li>
		<li class="nav-item"><a
			href="${pageContext.request.contextPath}/logout"
			class="nav-link text-danger"> <i class="fas fa-sign-out-alt"></i>
				Logout
		</a></li>
		

	</ul>
</nav>