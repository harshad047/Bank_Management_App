
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Arya Vikas Bank - Login</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<style>
body {
	background: linear-gradient(135deg, #e3f2fd, #bbdefb);
	font-family: 'Segoe UI', sans-serif;
	min-height: 100vh;
	display: flex;
	align-items: center;
	justify-content: center;
}

.login-card {
	border: none;
	border-radius: 15px;
	box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.card-header {
	background-color: #1565c0;
	color: white;
	text-align: center;
	border-radius: 15px 15px 0 0 !important;
	padding: 20px;
}

.btn-primary {
	background-color: #1565c0;
	border: none;
}

.btn-primary:hover {
	background-color: #0d47a1;
}

.form-control:focus {
	border-color: #1565c0;
	box-shadow: 0 0 0 0.2rem rgba(21, 101, 192, 0.25);
}
</style>
</head>
<body>
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-md-6 col-lg-4">
				<div class="card login-card">
					<div class="card-header">
						<h4>
							<i class="fas fa-university"></i> Arya Vikas Bank
						</h4>
						<small>Secure Banking Portal</small>
					</div>
					<div class="card-body p-4">

						<!-- Login Form -->
						<form id="loginForm"
							action="${pageContext.request.contextPath}/login" method="post">
							<h5 class="text-center mb-4">User Login</h5>

							<%
							if (request.getAttribute("error") != null) {
							%>
							<div class="alert alert-danger alert-sm">
								<i class="fas fa-exclamation-triangle"></i>
								<%=request.getAttribute("error")%>
							</div>
							<%
							}
							%>

							<div class="mb-3">
								<label for="username" class="form-label">Username</label>
								<div class="input-group">
									<span class="input-group-text"><i class="fas fa-user"></i></span>
									<input type="text" class="form-control" id="username"
										name="username" required autofocus />
								</div>
							</div>
							<div class="mb-3">
								<label for="password" class="form-label">Password</label>
								<div class="input-group">
									<span class="input-group-text"><i class="fas fa-lock"></i></span>
									<input type="password" class="form-control" id="password"
										name="password" required />
								</div>
							</div>
							<button type="submit" class="btn btn-primary w-100 mb-3">
								<i class="fas fa-sign-in-alt"></i> Login
							</button>
							<div class="text-center">
								<a href="?forgot=true" class="text-decoration-none small">Forgot
									Password?</a>
									<br>
									<a href="${pageContext.request.contextPath}/register/step1" class="text-decoration-none small">Don't have account? Register</a>
							</div>
						</form>

						<!-- Forgot Password Form -->
						<form id="forgotForm" action="forgot-password" method="post"
							${param.forgot == null ? 'style="display:none;"' : ''}>
							<input type="hidden" name="action" value="verify" />
							<h5 class="text-center mb-4">Reset Password</h5>

							<%
							if (request.getAttribute("error") != null && request.getAttribute("showReset") == null) {
							%>
							<div class="alert alert-danger alert-sm">
								<i class="fas fa-exclamation-triangle"></i>
								<%=request.getAttribute("error")%>
							</div>
							<%
							}
							%>

							<div class="mb-3">
								<label for="resetUsername" class="form-label">Username</label> <input
									type="text" class="form-control" id="resetUsername"
									name="username" required autofocus />
							</div>
							<div class="mb-3">
								<label for="questionId" class="form-label">Security
									Question</label> <select class="form-select" id="questionId"
									name="questionId" required>
									<option value="">-- Select Question --</option>
									<option value="1">What is your first school name?</option>
									<option value="2">What is the name of your favorite
										teacher?</option>
									<option value="3">What is your pet's name?</option>
									<option value="4">What is your mother's maiden name?</option>
								</select>
							</div>
							<div class="mb-3">
								<label for="answer" class="form-label">Answer</label> <input
									type="text" class="form-control" id="answer" name="answer"
									required />
							</div>
							<button type="submit" class="btn btn-primary w-100 mb-3">Verify</button>
							<div class="text-center">
								<a href="login" class="text-decoration-none small">Back to
									Login</a>
							</div>
						</form>

						<%
						if (request.getAttribute("success") != null) {
						%>
						<div class="alert alert-success">
							<i class="fas fa-check-circle"></i>
							<%=request.getAttribute("success")%>
						</div>
						<%
						}
						%>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script>
	    // Toggle between login and forgot password
	    const loginLink = document.querySelector('a[href="?forgot=true"]');
	    const forgotLink = document.querySelector('a[href="login"]');
	    const loginForm = document.getElementById('loginForm');
	    const forgotForm = document.getElementById('forgotForm');
	
	    if (loginLink) { 
	        loginLink.addEventListener('click', (e) => {
	            e.preventDefault();
	            loginForm.style.display = 'none';
	            forgotForm.style.display = 'block';
	        });
	    }
	    if (forgotLink) {
	        forgotLink.addEventListener('click', (e) => {
	            e.preventDefault();
	            forgotForm.style.display = 'none';
	            loginForm.style.display = 'block';
	        });
	    }
	</script>
</body>
</html>