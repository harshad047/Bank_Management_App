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

        <!-- Filter Section -->
        <div class="filter-section mb-4">
            <label><strong>Filter by Status:</strong></label>
            <select id="statusFilter" class="form-select" style="max-width: 300px;">
                <option value="">All</option>
                <option value="ACTIVE">Active</option>
                <option value="CLOSED">Closed</option>
            </select>
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
                        <tr data-status="<%= fd.getStatus() %>">
                            <td><strong>#<%= fd.getFdId() %></strong></td>
                            <td><%= fd.getUserId() %></td>
                            <td><%= String.format("%.2f", fd.getAmount()) %></td>
                            <td><%= fd.getTenureMonths() %> months</td>
                            <td><%= fd.getStartDate() %></td>
                            <td><%= fd.getMaturityDate() %></td>
                            <td>
                                <span class="badge 
                                <%= "ACTIVE".equals(fd.getStatus()) ? "bg-success" : "bg-secondary" %>">
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

<!-- Client-Side Filter Script -->
<script>
    document.getElementById("statusFilter").addEventListener("change", function () {
        const filter = this.value.toUpperCase();
        const rows = document.querySelectorAll("#fdTable tbody tr");

        rows.forEach(row => {
            const status = row.getAttribute("data-status");
            if (filter === "" || status.includes(filter)) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>