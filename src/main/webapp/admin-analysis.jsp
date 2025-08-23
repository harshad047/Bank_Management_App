<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Arya Vikas Bank - Admin Analytics</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { background-color: #f4f6f9; }
        .chart-container { max-width: 800px; margin: 30px auto; }
        .card { border-radius: 12px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .chart-small {
    max-width: 300px;   /* control width */
    max-height: 300px;  /* control height */
    margin: 0 auto;     /* center align */
}
        
    </style>
</head>
<body>
<div class="d-flex">

    <!-- Sidebar -->
    <jsp:include page="admin-sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-grow-1 p-4">
        <h2><i class="fas fa-chart-bar"></i> Admin Analytics Dashboard</h2>
        <hr>

        <!-- KPI Cards -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card bg-primary text-white">
                    <div class="card-body">
                        <h6>Total Customers</h6>
                        <h3>${totalUsers}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card bg-success text-white">
                    <div class="card-body">
                        <h6>Active Accounts</h6>
                        <h3>${approvedCount}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card bg-warning text-white">
                    <div class="card-body">
                        <h6>New Today</h6>
                        <h3>${newRegistrationsToday}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card bg-info text-white">
                    <div class="card-body">
                        <h6>Total Balance</h6>
                        <h3>₹${totalBalance}</h3>
                    </div>
                </div>
            </div>
        </div>

<div class="row">
    <!-- Credit vs Debit -->
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">Credit vs Debit Distribution</div>
            <div class="card-body text-center">
                <div class="chart-small">
                    <canvas id="transactionChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- User Status -->
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">User Status Distribution</div>
            <div class="card-body text-center">
                <div class="chart-small">
                    <canvas id="userStatusChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>


        <!-- Monthly Summary Table -->
        <div class="card mt-4">
            <div class="card-header">Monthly Transaction Summary</div>
            <div class="card-body">
                <table class="table table-bordered">
                    <thead class="table-dark">
                        <tr>
                            <th>Month</th>
                            <th>Total Credit (₹)</th>
                            <th>Total Debit (₹)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${monthlySummary}" var="ms">
                        <tr>
                            <td>${ms.label}</td>
                            <td>₹${ms.totalCredit}</td>
                            <td>₹${ms.totalDebit}</td>
                        </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Chart.js Scripts -->
<script>
    // --- Calculate total credits & debits ---
    const creditValues = [<c:forTokens items="${chartCredits}" delims="," var="amt">${amt},</c:forTokens>];
    const debitValues = [<c:forTokens items="${chartDebits}" delims="," var="amt">${amt},</c:forTokens>];

    const totalCredit = creditValues.reduce((a, b) => a + parseFloat(b), 0);
    const totalDebit = debitValues.reduce((a, b) => a + parseFloat(b), 0);

    // Transaction Pie Chart
    new Chart(document.getElementById('transactionChart'), {
        type: 'pie',
        data: {
            labels: ['Total Credit', 'Total Debit'],
            datasets: [{
                data: [totalCredit, totalDebit],
                backgroundColor: ['#28a745', '#dc3545']
            }]
        }
    });

    // User Status Pie Chart
    new Chart(document.getElementById('userStatusChart'), {
        type: 'pie',
        data: {
            labels: ['Approved', 'Pending', 'Rejected'],
            datasets: [{
                data: [${approvedCount}, ${pendingCount}, ${rejectedCount}],
                backgroundColor: ['#28a745', '#ffc107', '#dc3545']
            }]
        }
    });
</script>
</body>
</html>
