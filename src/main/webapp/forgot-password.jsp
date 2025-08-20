<!-- webapp/forgot-password.jsp -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Arya Vikas Bank - Forgot Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body {
            background: linear-gradient(135deg, #e3f2fd, #bbdefb);
            font-family: 'Segoe UI', sans-serif;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .reset-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
        .card-header {
            background-color: #d32f2f;
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
        .btn-success {
            background-color: #2e7d32;
            border: none;
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
        <div class="col-md-6 col-lg-5">
            <div class="card reset-card">
                <div class="card-header">
                    <h4><i class="fas fa-lock"></i> Reset Your Password</h4>
                    <small>Secure Account Recovery</small>
                </div>
                <div class="card-body p-4">

                    <!-- Step 1: Verify User & Security Answer -->
                    <% if (request.getAttribute("showReset") == null && request.getParameter("showReset") == null) { %>
                    <form action="forgot-password" method="post">
                        <input type="hidden" name="action" value="verify" />
                        <h5 class="text-center mb-4">Step 1: Verify Identity</h5>

                        <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger alert-sm">
                            <i class="fas fa-exclamation-triangle"></i>
                            <%= request.getAttribute("error") %>
                        </div>
                        <% } %>

                        <div class="mb-3">
                            <label for="username" class="form-label">Username</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-user"></i></span>
                                <input type="text" class="form-control" id="username" name="username" required autofocus />
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="questionId" class="form-label">Security Question</label>
                            <select class="form-select" id="questionId" name="questionId" required>
                                <option value="">-- Select Question --</option>
                                <option value="1">What is your first school name?</option>
                                <option value="2">What is the name of your favorite teacher?</option>
                                <option value="3">What is your pet's name?</option>
                                <option value="4">What is your mother's maiden name?</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="answer" class="form-label">Answer</label>
                            <input type="text" class="form-control" id="answer" name="answer" required />
                        </div>

                        <button type="submit" class="btn btn-primary w-100 mb-3">
                            <i class="fas fa-check"></i> Verify
                        </button>
                        <div class="text-center">
                            <a href="login" class="text-decoration-none small">Back to Login</a>
                        </div>
                    </form>

                    <!-- Step 2: Reset Password -->
                    <% } else { %>
                    <form action="forgot-password" method="post">
                        <input type="hidden" name="action" value="reset" />
                        <h5 class="text-center mb-4">Step 2: Set New Password</h5>

                        <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger alert-sm">
                            <i class="fas fa-exclamation-triangle"></i>
                            <%= request.getAttribute("error") %>
                        </div>
                        <% } %>

                        <% if (request.getAttribute("success") != null) { %>
                        <div class="alert alert-success alert-sm">
                            <i class="fas fa-check-circle"></i>
                            <%= request.getAttribute("success") %>
                        </div>
                        <% } %>

                        <div class="mb-3">
                            <label for="newPassword" class="form-label">New Password</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                <input type="password" class="form-control" id="newPassword" name="newPassword" minlength="6" required />
                            </div>
                            <small class="text-muted">Must be at least 6 characters</small>
                        </div>

                        <div class="mb-3">
                            <label for="confirmNewPassword" class="form-label">Confirm New Password</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                <input type="password" class="form-control" id="confirmNewPassword" name="confirmNewPassword" required />
                            </div>
                        </div>

                        <button type="submit" class="btn btn-success w-100 mb-3">
                            <i class="fas fa-key"></i> Change Password
                        </button>
                        <div class="text-center">
                            <a href="login" class="text-decoration-none small">Back to Login</a>
                        </div>
                    </form>
                    <% } %>

                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>