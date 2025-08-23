<%@ page import="java.util.List" %>
<%@ page import="com.tss.model.FdApplication" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<FdApplication> pendingApps = (List<FdApplication>) request.getAttribute("pendingApps");
    if (pendingApps == null) pendingApps = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Approve FD Applications</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f4f6f9; }
        .sidebar { background: #1a2537; }
        .table th { background-color: #2f3b52; color: white; }
        .btn-sm i { margin-right: 4px; }
    </style>
</head>
<body>
<div class="d-flex">
    <jsp:include page="admin-sidebar.jsp"/>

    <div class="flex-grow-1 p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-check-circle"></i> Approve FD Applications</h2>
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

        <!-- Pending Applications Table -->
        <% if (pendingApps.isEmpty()) { %>
            <div class="alert alert-info">No pending FD applications.</div>
        <% } else { %>
            <div class="card shadow-sm">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th>App ID</th>
                            <th>User ID</th>
                            <th>Amount (₹)</th>
                            <th>Tenure</th>
                            <th>Rate</th>
                            <th>Applied On</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (FdApplication app : pendingApps) { %>
                        <tr>
                            <td><strong>#<%= app.getFdAppId() %></strong></td>
                            <td><%= app.getUserId() %></td>
                            <td><%= String.format("%.2f", app.getAmount()) %></td>
                            <td><%= app.getTenureMonths() %> months</td>
                            <td><%= app.getInterestRate() %>%</td>
                            <td><%= app.getApplicationDate().toLocalDate() %></td>
                            <td>
                                <a href="../approve-fd?action=approve&fdAppId=<%= app.getFdAppId() %>"
                                   class="btn btn-success btn-sm"
                                   onclick="return confirm('Approve this FD application?');">
                                    <i class="fas fa-check"></i> Approve
                                </a>
                                <a href="../approve-fd?action=reject&fdAppId=<%= app.getFdAppId() %>"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Reject this FD application?');">
                                    <i class="fas fa-times"></i> Reject
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