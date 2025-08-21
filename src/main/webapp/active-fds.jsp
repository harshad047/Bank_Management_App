<%@ page import="java.util.List" %>
<%@ page import="com.tss.model.FixedDeposit" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<FixedDeposit> activeFds = (List<FixedDeposit>) request.getAttribute("activeFds");
    if (activeFds == null) activeFds = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Active Fixed Deposits</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f4f6f9; }
        .sidebar { background: #1a2537; }
        .table-striped > thead > tr > th { background-color: #2f3b52; color: white; }
        .badge-active { background-color: #28a745; }
        .card { border-radius: 12px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
    </style>
</head>
<body>
<div class="d-flex">
    <jsp:include page="admin-sidebar.jsp"/>

    <div class="flex-grow-1 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-box"></i> Active Fixed Deposits</h2>
            <a href="manage-fds" class="btn btn-outline-secondary btn-sm">
                <i class="fas fa-arrow-left"></i> Back
            </a>
        </div>

        <!-- Messages -->
        <% if (session.getAttribute("message") != null) { %>
            <div class="alert alert-success alert-dismissible fade show">
                <%= session.getAttribute("message") %>
                <% session.removeAttribute("message"); %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>
        <% if (session.getAttribute("error") != null) { %>
            <div class="alert alert-danger alert-dismissible fade show">
                <%= session.getAttribute("error") %>
                <% session.removeAttribute("error"); %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>

        <!-- Active FDs Table -->
        <% if (activeFds.isEmpty()) { %>
            <div class="alert alert-info">No active fixed deposits found.</div>
        <% } else { %>
            <div class="card shadow-sm">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th>FD ID</th>
                            <th>User ID</th>
                            <th>Amount (₹)</th>
                            <th>Tenure</th>
                            <th>Start → Maturity</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (FixedDeposit fd : activeFds) { %>
                        <tr>
                            <td><strong>#<%= fd.getFdId() %></strong></td>
                            <td><%= fd.getUserId() %></td>
                            <td><%= String.format("%.2f", fd.getAmount()) %></td>
                            <td><%= fd.getTenureMonths() %> months</td>
                            <td><%= fd.getStartDate() %> → <%= fd.getMaturityDate() %></td>
                            <td><span class="badge bg-success">ACTIVE</span></td>
                            <td>
                                <a href="manage-fds?action=close&fdId=<%= fd.getFdId() %>"
                                   class="btn btn-warning btn-sm"
                                   onclick="return confirm('Close this FD? This cannot be undone.');">
                                    <i class="fas fa-door-closed"></i> Close FD
                                </a>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>