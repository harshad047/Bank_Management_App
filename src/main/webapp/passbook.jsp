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
        .no-print { margin-bottom: 20px; }
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

        <!-- Export Buttons -->
        <div class="text-end no-print">
            <button class="btn btn-danger btn-sm" onclick="downloadPDF()">
                <i class="fas fa-file-pdf"></i> Download PDF
            </button>
            <button class="btn btn-success btn-sm" onclick="downloadExcel()">
                <i class="fas fa-file-excel"></i> Download Excel
            </button>
        </div>

        <!-- Transactions Table -->
        <h5>Recent Transactions</h5>
        <table id="txnTable" class="table table-bordered transactions-table">
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
    </div>
</div>

<!-- Export Libraries -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.25/jspdf.plugin.autotable.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>
<script src="https://kit.fontawesome.com/a2e0e6ad62.js" crossorigin="anonymous"></script>

<script>
function downloadPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    doc.text("Arya Vikas Bank - Passbook", 14, 10);
    doc.text("Account: ${account.accountHolderName} (${account.accountNumber})", 14, 18);
    doc.autoTable({ html: "#txnTable", startY: 28 });
    doc.save("passbook.pdf");
}

function downloadExcel() {
    var table = document.getElementById("txnTable");
    var wb = XLSX.utils.table_to_book(table, { sheet: "Passbook" });
    XLSX.writeFile(wb, "passbook.xlsx");
}
</script>

</body>
</html>
