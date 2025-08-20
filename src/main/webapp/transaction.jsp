<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>User Transactions</title>
<meta name="csrf-token" content="${sessionScope.csrfToken}" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
<style>
    body { background-color: #f8f9fa; }
    .sidebar { background-color: #1565c0; min-height: 100vh; }
    .sidebar a { color: white; }
    .sidebar a:hover { background-color: #0d47a1; }
</style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="user-sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-grow-1 p-4">
        <h2>
            <i class="fas fa-exchange-alt"></i> Perform Transaction
        </h2>
        <hr />

        <!-- ✅ Success / Error Messages -->
        <c:if test="${param.success == '1'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ✅ Transaction completed successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <c:if test="${not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ❌ ${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="row">
            <!-- Credit Form -->
            <div class="col-md-6">
                <div class="card border-success mb-3">
                    <div class="card-header bg-success text-white">
                        <i class="fas fa-arrow-down"></i> Credit (Deposit)
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/user/transaction" method="post">
                            <input type="hidden" name="action" value="credit" />
                            <div class="mb-3">
                                <label for="creditAmount" class="form-label">Amount</label>
                                <input type="number" step="0.01" min="1" name="amount" id="creditAmount"
                                       class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="creditDesc" class="form-label">Description</label>
                                <input type="text" name="description" id="creditDesc" class="form-control">
                            </div>
                            <button type="submit" class="btn btn-success w-100">Deposit</button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Debit Form -->
            <div class="col-md-6">
                <div class="card border-danger mb-3">
                    <div class="card-header bg-danger text-white">
                        <i class="fas fa-arrow-up"></i> Debit (Withdraw)
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/user/transaction" method="post">
                            <input type="hidden" name="action" value="debit" />
                            <div class="mb-3">
                                <label for="debitAmount" class="form-label">Amount</label>
                                <input type="number" step="0.01" min="1" name="amount" id="debitAmount"
                                       class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="debitDesc" class="form-label">Description</label>
                                <input type="text" name="description" id="debitDesc" class="form-control">
                            </div>
                            <button type="submit" class="btn btn-danger w-100">Withdraw</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Show Transaction History -->
        <h3 class="mt-4">Recent Transactions</h3>
        <div class="d-flex justify-content-between align-items-center">
            <h3 class="mt-4">Recent Transactions</h3>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/user/export-transactions.csv"><i class="fas fa-file-csv"></i> Export CSV</a>
        </div>
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Description</th>
                    <th>Date</th>
                    <th>Balance After</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="txn" items="${transactions}">
                    <tr>
                        <td>${txn.txnId}</td>
                        <td>${txn.txnType}</td>
                        <td>₹${txn.amount}</td>
                        <td>${txn.description}</td>
                        <td>${txn.txnTime}</td>
                        <td>₹${txn.balanceAfter}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/csrf.js"></script>
</body>
</html>
