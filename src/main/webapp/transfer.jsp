<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.math.BigDecimal" %>
<html>
<head>
    <title>Fund Transfer - Arya Vikas Bank</title>
    <meta name="csrf-token" content="${sessionScope.csrfToken}" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f8f9fa; }
        .sidebar { background-color: #1565c0; min-height: 100vh; }
        .sidebar a { color: white; }
        .sidebar a:hover { background-color: #0d47a1; }
    </style>
    <script>
       
    </script>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="user-sidebar.jsp" />

    <!-- Main Content -->
    <div class="main flex-grow-1">
        <div class="container">
            <h3 class="mb-4"><i class="fas fa-money-check-alt"></i> Fund Transfer</h3>

            <!-- Show error/success messages -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <c:set var="balance" value="${sessionScope.balance}" />

            <!-- Transfer Form -->
            <form action="${pageContext.request.contextPath}/user/transfer" method="post" class="card p-4 shadow-sm" onsubmit="return validateForm()">
                <input type="hidden" id="currentBalance" value="${balance}"/>

                <div class="mb-3">
                    <label for="receiverId" class="form-label">Select Receiver</label>
                    <select name="receiverId" id="receiverId" class="form-select" required>
                        <option value="">-- Select Account --</option>
                        <c:forEach var="acc" items="${accounts}">
                            <option value="${acc.accountId}">
                                ${acc.accountNumber} - ${acc.accountHolderName} (${acc.accountType})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="amount" class="form-label">Amount</label>
<input type="number" step="0.01" name="amount" id="amount" min="1" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Description (Optional)</label>
                    <input type="text" name="description" id="description" class="form-control"/>
                </div>

                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-paper-plane"></i> Transfer
                </button>
            </form>

            <!-- Transfer History -->
            <h4 class="mt-5 mb-3"><i class="fas fa-history"></i> Transfer History</h4>
            <c:if test="${not empty transfers}">
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>From</th>
                            <th>To</th>
                            <th>Amount</th>
                            <th>Description</th>
                            <th>Date & Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="t" items="${transfers}">
                            <tr>
                                <td>${t.transferId}</td>
                                <td>${t.fromAccountId}</td>
                                <td>${t.toAccountId}</td>
                                <td>${t.amount}</td>
                                <td>${t.description}</td>
                                <td>${t.transferTime}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty transfers}">
                <div class="alert alert-info">No transfers yet.</div>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>
