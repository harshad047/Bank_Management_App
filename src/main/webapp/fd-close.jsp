<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
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
    <title>Close Active FDs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f4f6f9; }
        .sidebar { background: #1a2537; }
        .table th { background-color: #2f3b52; color: white; }
        .btn-close-fd {
            background-color: #dc3545;
            color: white;
            border: none;
        }
        .btn-close-fd:hover {
            background-color: #c82333;
        }
        .badge-remaining { background-color: #0d6efd; }
    </style>
</head>
<body>
<div class="d-flex">
    <jsp:include page="admin-sidebar.jsp"/>

    <div class="flex-grow-1 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-door-open"></i> Close Active FDs</h2>
            <a href="${pageContext.request.contextPath}/admin/manage-fds" class="btn btn-outline-secondary btn-sm">
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
                            <th>Maturity Date</th>
                            <th>Remaining</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (FixedDeposit fd : activeFds) { %>
                        <%
                            LocalDate today = LocalDate.now();
                            long daysLeft = ChronoUnit.DAYS.between(today, fd.getMaturityDate());
                            String badgeColor = daysLeft < 30 ? "bg-danger" : "bg-success";
                        %>
                        <tr>
                            <td><strong>#<%= fd.getFdId() %></strong></td>
                            <td><%= fd.getUserId() %></td>
                            <td><%= String.format("%.2f", fd.getAmount()) %></td>
                            <td><%= fd.getTenureMonths() %> months</td>
                            <td><%= fd.getMaturityDate() %></td>
                            <td>
                                <span class="badge <%= badgeColor %>">
                                    <%= daysLeft > 0 ? daysLeft + " days left" : "Overdue" %>
                                </span>
                            </td>
                            <td>
                                <!-- ✅ Fixed: Use GET and correct URL -->
                                <a href="${pageContext.request.contextPath}/close-fd?fdId=<%= fd.getFdId() %>"
                                   class="btn btn-close-fd btn-sm"
                                   onclick="return confirm('Close this FD? Maturity amount will be credited to user account.');">
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