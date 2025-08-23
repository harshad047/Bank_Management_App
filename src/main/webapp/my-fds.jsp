<%@ page import="java.util.List" %>
<%@ page import="com.tss.dto.UserFdView" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<UserFdView> fdSummary = (List<UserFdView>) request.getAttribute("fdSummary");
    if (fdSummary == null) fdSummary = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>My Fixed Deposits</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    <style>
        body {
            background-color: #f8f9fa;
        }
        .sidebar {
            background-color: #1565c0;
            min-height: 100vh;
        }
        .main-content {
            padding: 2rem;
        }
        .card-pending { border-left: 4px solid #ffc107; }
        .card-active { border-left: 4px solid #28a745; }
        .card-matured { border-left: 4px solid #17a2b8; }
        .card-closed { border-left: 4px solid #6c757d; }
        .card-rejected { border-left: 4px solid #dc3545; } /* Red */
        .sidebar a { color: white; }
        .sidebar a:hover { background-color: #0d47a1; }
        .card h2, .card h5 { font-weight: bold; }
        .badge { margin-top: 5px; }
    </style>
</head>
<body>
    <div class="d-flex">
        <jsp:include page="user-sidebar.jsp" />
        
        <div class="flex-grow-1 main-content">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="fas fa-box"></i> My Fixed Deposits</h2>
            </div>

            <% if (fdSummary.isEmpty()) { %>
                <div class="alert alert-info">You have no fixed deposits yet.</div>
            <% } else { %>
                <div class="row">
                    <% for (UserFdView fd : fdSummary) { %>
                        <!-- Skip APPROVED status entirely -->
                        <% if ("APPROVED".equals(fd.getStatus())) { continue; } %>

                        <!-- Skip dummy CLOSED rows (null dates & zero maturity) -->
                        <% if ("CLOSED".equals(fd.getStatus()) && 
                              (fd.getStartDate() == null || fd.getMaturityDate() == null || fd.getMaturityAmount() <= 0)) { 
                            continue; 
                        } %>

                        <div class="col-md-6 mb-3">
                            <div class="card h-100 shadow-sm 
                                <%="PENDING".equals(fd.getStatus()) ? "card-pending" : 
                                   "ACTIVE".equals(fd.getStatus()) ? "card-active" : 
                                   "MATURED".equals(fd.getStatus()) ? "card-matured" : 
                                   "CLOSED".equals(fd.getStatus()) ? "card-closed" : 
                                   "REJECTED".equals(fd.getStatus()) ? "card-rejected" : ""%>">
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <% if ("PENDING".equals(fd.getStatus())) { %>
                                            📌 Application (Pending Approval)
                                        <% } else if ("REJECTED".equals(fd.getStatus())) { %>
                                            🚫 Fixed Deposit (Rejected)
                                        <% } else { %>
                                            <% if ("ACTIVE".equals(fd.getStatus())) { %>
                                                ✅ Fixed Deposit (Active)
                                            <% } else if ("MATURED".equals(fd.getStatus())) { %>
                                                🎉 Fixed Deposit (Matured)
                                            <% } else if ("CLOSED".equals(fd.getStatus())) { %>
                                                🚫 Fixed Deposit (Closed)
                                            <% } %>
                                        <% } %>
                                    </h5>

                                    <p class="card-text">
                                        <strong>Amount:</strong> ₹<%= String.format("%.2f", fd.getAmount()) %><br>

                                        <strong>Tenure:</strong> <%= fd.getTenureMonths() %> months<br>

                                        <strong>Interest Rate:</strong> <%= fd.getInterestRate() %>%<br>

                                        <% if ("PENDING".equals(fd.getStatus())) { %>
                                            <% if (fd.getApplicationDate() != null) { %>
                                                <strong>Applied On:</strong> <%= fd.getApplicationDate().toLocalDate() %><br>
                                            <% } %>
                                            <span class="badge bg-warning text-dark">Pending Approval</span>
                                        <% } else if ("REJECTED".equals(fd.getStatus())) { %>
                                            <span class="badge bg-danger">Rejected</span>
                                        <% } else { %>
                                            <% if (fd.getStartDate() != null) { %>
                                                <strong>Start Date:</strong> <%= fd.getStartDate() %><br>
                                            <% } %>
                                            <% if (fd.getMaturityDate() != null) { %>
                                                <strong>Maturity Date:</strong> <%= fd.getMaturityDate() %><br>
                                            <% } %>
                                            <% if (fd.getMaturityAmount() > 0) { %>
                                                <strong>Maturity Amount:</strong> ₹<%= String.format("%.2f", fd.getMaturityAmount()) %><br>
                                            <% } %>
                                            
                                            <% if ("ACTIVE".equals(fd.getStatus())) { %>
                                                <span class="badge bg-success">Active</span>
                                            <% } else if ("MATURED".equals(fd.getStatus())) { %>
                                                <span class="badge bg-primary">Matured</span>
                                            <% } else if ("CLOSED".equals(fd.getStatus())) { %>
                                                <span class="badge bg-secondary">Closed</span>
                                            <% } %>
                                        <% } %>
                                    </p>
                                </div>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>