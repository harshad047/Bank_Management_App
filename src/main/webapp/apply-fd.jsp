<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Apply for Fixed Deposit</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f8f9fa; }
        .sidebar { background-color: #1565c0; min-height: 100vh; }
        .sidebar a { color: white; }
        .sidebar a:hover { background-color: #0d47a1; }
        .main-content { padding: 2rem; }
        .card { border: none; border-radius: 12px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
    </style>
</head>
<body>
<div class="d-flex">
    <jsp:include page="user-sidebar.jsp"/>

    <div class="flex-grow-1 main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-certificate"></i> Apply for Fixed Deposit</h2>
        </div>

        <!-- ✅ Show Success or Error Message -->
        <% 
            String message = (String) session.getAttribute("message");
            String error = (String) session.getAttribute("error");
            if (message != null) {
        %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <%= message %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% 
            session.removeAttribute("message"); // One-time display
            } 
            if (error != null) {
        %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% 
            session.removeAttribute("error"); 
            } 
        %>

        <div class="card shadow-sm">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/user/apply-fd" method="post">
                    <input type="hidden" name="userId" value="${sessionScope.user_id}">
                    <input type="hidden" name="accountId" value="${sessionScope.account_id}">

                    <div class="mb-3">
                        <label class="form-label">Deposit Amount (₹)</label>
                        <input type="number" name="amount" class="form-control" min="1000" step="100" required>
                        <div class="form-text text-success">Minimum ₹1,000. Multiples of ₹100.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Tenure & Interest Rate</label>
                        <select name="tenureMonths" class="form-select" required>
                            <option value="6">6 Months – 6.5% per annum</option>
                            <option value="12">1 Year – 6.8% per annum</option>
                            <option value="24">2 Years – 7.0% per annum</option>
                            <option value="36">3 Years – 7.1% per annum</option>
                            <option value="60">5 Years – 7.5% per annum</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-check-circle"></i> Submit Application
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS (for alert close) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>