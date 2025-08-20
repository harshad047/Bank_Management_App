<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Transaction Report</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f4f6f9; }
        .sidebar { background: #1a2537; min-height: 100vh; }
        .sidebar a { color: #dfe4ea; }
        .sidebar a:hover { background: #2f3b52; }
    </style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="admin-sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-grow-1 p-4">
        <h2><i class="fas fa-file-invoice"></i> Transaction Report</h2>
        <hr>

        <!-- Filters -->
        <form method="get" class="row g-3 mb-4">
            <div class="col-md-2">
                <input type="number" step="0.01" name="minAmount" class="form-control" placeholder="Min Amount">
            </div>
            <div class="col-md-2">
                <input type="number" step="0.01" name="maxAmount" class="form-control" placeholder="Max Amount">
            </div>
            <div class="col-md-2">
                <input type="date" name="fromDate" class="form-control">
            </div>
            <div class="col-md-2">
                <input type="date" name="toDate" class="form-control">
            </div>
            <div class="col-md-2">
                <select name="accountType" class="form-control">
                    <option value="">All Accounts</option>
                    <option value="SAVINGS">SAVINGS</option>
                    <option value="CURRENT">CURRENT</option>
                </select>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Filter</button>
            </div>
        </form>

        <!-- Transactions Table -->
        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>User ID</th>
                    <th>Account ID</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Description</th>
                    <th>Date</th>
                    <th>Balance After</th>
                    <th>Channel</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="t" items="${transactions}">
                    <tr>
                        <td>${t.txnId}</td>
                        <td>${t.userId}</td>
                        <td>${t.accountId}</td>
                        <td>${t.txnType}</td>
                        <td>${t.amount}</td>
                        <td>${t.description}</td>
                        <td>${t.txnTime}</td>
                        <td>${t.balanceAfter}</td>
                        <td>${t.channel}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </div>
</div>
</body>
</html>
