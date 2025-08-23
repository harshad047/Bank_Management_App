<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Arya Vikas Bank - User Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f8f9fa; }
        .sidebar { background-color: #1565c0; min-height: 100vh; }
        .sidebar a { color: white; }
        .sidebar a:hover { background-color: #0d47a1; }
        #transactionPieChart {
            max-width: 250px;
            max-height: 250px;
            margin: 0 auto;
            display: block;
        }
        .form-card { border: 1px solid #dee2e6; border-radius: 8px; }
        .quick-action-btn {
            margin-bottom: 10px;
            font-size: 16px;
            padding: 12px;
        }
    </style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="user-sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-grow-1 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-user"></i> Welcome, ${user.username}!</h2>
            <span class="badge bg-primary">Account: ${account.accountNumber}</span>
        </div>

        <!-- Account Info Cards -->
        <div class="row">
            <div class="col-md-4">
                <div class="card text-white bg-primary">
                    <div class="card-body">
                        <h5>Balance</h5>
                        <h2>₹${account.balance}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-success">
                    <div class="card-body">
                        <h5>Account Type</h5>
                        <h2>${user.accountType}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-info">
                    <div class="card-body">
                        <h5>Status</h5>
                        <h2>${user.status}</h2>
                    </div>
                </div>
            </div>
        </div>

        <!-- Pie Chart + Quick Actions Side by Side -->
        <div class="row mt-4 g-3">
            <!-- Pie Chart -->
            <div class="col-md-6 d-flex">
                <div class="card shadow-sm w-100 h-100">
                    <div class="card-header bg-secondary text-white">
                        Last Month Credit vs Debit
                    </div>
                    <div class="card-body text-center">
                        <canvas id="transactionPieChart"></canvas>
                        <p class="mt-3 text-muted small">
                            Showing transactions for the last 30 days.
                        </p>
                    </div>
                </div>
            </div>

            <!-- Quick Actions -->
            <div class="col-md-6 d-flex">
                <div class="card shadow-sm w-100 h-100">
                    <div class="card-header bg-dark text-white">
                        <i class="fas fa-bolt"></i> Quick Actions
                    </div>
                    <div class="card-body d-flex flex-column justify-content-center">
                        <a href="${pageContext.request.contextPath}/user/transfer" class="btn btn-primary quick-action-btn">
                            <i class="fas fa-money-check-alt"></i> Transfer Money
                        </a>
                        <a href="${pageContext.request.contextPath}/user/passbook" class="btn btn-success quick-action-btn">
                            <i class="fas fa-book"></i> View Passbook
                        </a>
                        <a href="${pageContext.request.contextPath}/user/apply-fd" class="btn btn-warning quick-action-btn">
                            <i class="fas fa-hand-holding-usd"></i> Apply FD
                        </a>
                        <a href="${pageContext.request.contextPath}/user-profile" class="btn btn-info quick-action-btn">
                            <i class="fas fa-user"></i> Profile
                        </a>
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger quick-action-btn">
                            <i class="fas fa-sign-out-alt"></i> Logout
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    const ctx = document.getElementById('transactionPieChart').getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Credited', 'Debited'],
            datasets: [{
                data: [${credited}, ${debited}],
                backgroundColor: ['#28a745', '#dc3545']
            }]
        },
        options: {
            responsive: false,
            plugins: {
                legend: { position: 'bottom' }
            }
        }
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
