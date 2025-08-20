<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Arya Vikas Bank - Passbook</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
    body { font-family: Arial, sans-serif; background-color: #f2f5f7; }
    .sidebar { background-color: #1565c0; min-height: 100vh; }
    .sidebar a { color: white; text-decoration: none; display: block; padding: 12px; }
    .sidebar a:hover { background-color: #0d47a1; }
    .passbook-header { text-align: center; margin-bottom: 30px; }
    .passbook-header h2 { color: #0047b3; font-weight: bold; }
    .account-card { margin-bottom: 30px; }
    .transactions-table th, .transactions-table td { text-align: center; }
    .card h2 { font-weight: bold; }
    .main-content { flex-grow: 1; padding: 20px; }

    @media print {
        .no-print { display: none; }       /* Hides print button */
        .sidebar { display: none; }       /* Hides sidebar */
        body { background-color: white; }  /* Remove background for print */
    }
</style>
</head>
<body>
<div class="d-flex">

    <!-- Sidebar -->
    <div class="sidebar">
        <jsp:include page="user-sidebar.jsp" />
    </div>

    <!-- Main Content -->
    <div class="main-content">

        <!-- Header -->
        <div class="passbook-header">
            <h2>Arya Vikas Bank</h2>
            <p><strong>Official Passbook</strong></p>
        </div>

        <!-- Account Info -->
        <div class="card account-card">
            <div class="card-body">
                <h5>Account Holder Details</h5>
                <p><strong>Name:</strong> ${account.accountHolderName}</p>
                <p><strong>Account Number:</strong> ${account.accountNumber}</p>
                <p><strong>Account Type:</strong> ${account.accountType}</p>
                <p><strong>Status:</strong> ${account.status}</p>
                <p><strong>Current Balance:</strong> ₹${account.balance}</p>
            </div>
        </div>

        <!-- Transactions Table -->
        <h5>Recent Transactions</h5>
        <table class="table table-bordered transactions-table">
            <thead class="table-dark">
                <tr>
                    <th>Date & Time</th>
                    <th>Type</th>
                    <th>Description</th>
                    <th>Amount (₹)</th>
                    <th>Balance After (₹)</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="txn" items="${transactions}">
                    <tr>
                        <td>${txn.txnTime}</td>
                        <td>${txn.txnType}</td>
                        <td>${txn.description}</td>
                        <td>${txn.amount}</td>
                        <td>${txn.balanceAfter}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Print Button -->
        <div class="text-center my-4 no-print">
            <button onclick="window.print()" class="btn btn-primary">Print Passbook</button>
        </div>

    </div>
</div>
</body>
</html>
