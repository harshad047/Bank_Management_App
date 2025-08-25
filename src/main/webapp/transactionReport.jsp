<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Transaction Report</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.bootstrap5.min.css">

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
        <div class="card shadow-sm">
            <div class="table-responsive p-3">
                <table id="txnTable" class="table table-striped table-bordered">
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
</div>

<!-- jQuery + DataTables -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

<!-- Export Buttons -->
<script src="https://cdn.datatables.net/buttons/2.4.1/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.bootstrap5.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.html5.min.js"></script>

<script>
    $(document).ready(function () {
        $('#txnTable').DataTable({
            "pageLength": 5,
            "lengthMenu": [10, 25, 50, 100],
            "ordering": true,
            "searching": true,
            "language": {
                "search": "Search Transactions:"
            },
            dom: 'Bfrtip',
            buttons: [
                {
                    extend: 'excelHtml5',
                    text: '<i class="fas fa-file-excel"></i> Export Excel',
                    className: 'btn btn-success btn-sm'
                },
                {
                    extend: 'pdfHtml5',
                    text: '<i class="fas fa-file-pdf"></i> Export PDF',
                    className: 'btn btn-danger btn-sm',
                    orientation: 'landscape',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':visible'
                    }
                }
            ]
        });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
