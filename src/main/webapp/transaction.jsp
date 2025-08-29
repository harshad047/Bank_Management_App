<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>User Transactions</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.bootstrap5.min.css"/>
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
        <h2><i class="fas fa-exchange-alt"></i> Perform Transaction</h2>
        <hr />

        <!-- ✅ Success / Error Messages -->
        <c:if test="${param.success == '1'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ✅ Transaction completed successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <c:if test="${not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ❌ ${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- ✅ Action Buttons (Centered) -->
        <div class="mb-3 d-flex justify-content-center">
            <button class="btn btn-success me-2" type="button" data-bs-toggle="collapse"
                    data-bs-target="#depositForm" aria-expanded="false" aria-controls="depositForm">
                <i class="fas fa-arrow-down"></i> Deposit
            </button>
            <button class="btn btn-danger" type="button" data-bs-toggle="collapse"
                    data-bs-target="#withdrawForm" aria-expanded="false" aria-controls="withdrawForm">
                <i class="fas fa-arrow-up"></i> Withdraw
            </button>
        </div>

        <!-- ✅ Container for forms -->
        <div id="formsContainer">
            <!-- Deposit Form -->
            <div class="collapse" id="depositForm" data-bs-parent="#formsContainer">
                <div class="card border-success mx-auto mb-3" style="max-width: 500px;">
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

            <!-- Withdraw Form -->
            <div class="collapse" id="withdrawForm" data-bs-parent="#formsContainer">
                <div class="card border-danger mx-auto mb-3" style="max-width: 500px;">
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

        <!-- ✅ Transaction History -->
        <h3 class="mt-4">Recent Transactions</h3>
        <table id="transactionTable" class="table table-bordered table-striped">
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
                        <td>
                            <c:choose>
                                <c:when test="${txn.txnType == 'CREDIT'}">
                                    <span class="text-success fw-bold">Credit</span>
                                </c:when>
                                <c:when test="${txn.txnType == 'DEBIT'}">
                                    <span class="text-danger fw-bold">Debit</span>
                                </c:when>
                            </c:choose>
                        </td>
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

<!-- Bootstrap + jQuery + DataTables -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.bootstrap5.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.html5.min.js"></script>

<script>
    $(document).ready(function () {
        $('#transactionTable').DataTable({
            dom: '<"d-flex justify-content-between mb-2"Bf>rt<"d-flex justify-content-between mt-2"lip>',
            buttons: [
                { extend: 'excelHtml5', text: '<i class="fas fa-file-excel"></i> Excel', className: 'btn btn-success' },
                { extend: 'pdfHtml5', text: '<i class="fas fa-file-pdf"></i> PDF', className: 'btn btn-danger' }
            ],
            pageLength: 5,
            lengthMenu: [5, 10, 25, 50],
            ordering: false
        });
    });
</script>
</body>
</html>
