<%@ page import="java.util.List" %>
<%@ page import="com.tss.model.FixedDeposit" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<FixedDeposit> allFds = (List<FixedDeposit>) request.getAttribute("allFds");
    if (allFds == null) allFds = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>All Fixed Deposits</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.bootstrap5.min.css">

    <style>
        body { background-color: #f4f6f9; }
        .sidebar { background: #1a2537; }
        .table th { background-color: #2f3b52; color: white; }
        .filter-section { background-color: #fff; padding: 1rem; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
    </style>
</head>
<body>
<div class="d-flex">
    <jsp:include page="admin-sidebar.jsp"/>

    <div class="flex-grow-1 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-list"></i> All Fixed Deposits</h2>
            <a href="${pageContext.request.contextPath}/admin/manage-fds" class="btn btn-outline-secondary btn-sm">
                <i class="fas fa-arrow-left"></i> Back
            </a>
        </div>
        <!-- All FDs Table -->
        <% if (allFds.isEmpty()) { %>
            <div class="alert alert-info">No fixed deposits found.</div>
        <% } else { %>
            <div class="card shadow-sm">
                <div class="table-responsive">
                    <table class="table table-bordered mb-0" id="fdTable">
                        <thead class="table-dark">
                        <tr>
                            <th>FD ID</th>
                            <th>User ID</th>
                            <th>Amount (₹)</th>
                            <th>Tenure</th>
                            <th>Start Date</th>
                            <th>Maturity Date</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (FixedDeposit fd : allFds) { %>
                        <tr>
                            <td><strong>#<%= fd.getFdId() %></strong></td>
                            <td><%= fd.getUserId() %></td>
                            <td><%= String.format("%.2f", fd.getAmount()) %></td>
                            <td><%= fd.getTenureMonths() %> months</td>
                            <td><%= fd.getStartDate() %></td>
                            <td><%= fd.getMaturityDate() %></td>
                            <td>
                                <span class="badge <%= "ACTIVE".equals(fd.getStatus()) ? "bg-success" : "bg-secondary" %>">
                                    <%= fd.getStatus() %>
                                </span>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        <% } %>
    </div>
</div>

<!-- jQuery + DataTables -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>


<script>
    $(document).ready(function () {
        var table = $('#fdTable').DataTable({
            "pageLength": 5,
            "lengthMenu": [5, 10, 25, 50],
            "ordering": true,
            "searching": true,
            "language": {
                "search": "Search FDs:"
            },
            dom: 'Bfrtip',
            buttons: ['copy', 'excel', 'pdf', 'print']
        });

        // Custom Status Filter
        $('#statusFilter').on('change', function () {
            var status = this.value;
            if (status) {
                table.column(6).search('^' + status + '$', true, false).draw();
            } else {
                table.column(6).search('').draw();
            }
        });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
