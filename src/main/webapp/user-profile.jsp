<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Arya Vikas Bank - User Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f8f9fa; }
        .sidebar { background-color: #1565c0; min-height: 100vh; }
        .sidebar a { color: white; }
        .sidebar a:hover { background-color: #0d47a1; }
        .card h2 { font-weight: bold; }
    </style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="user-sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-grow-1 p-4">
        <div class="mb-4">
            <h2><i class="fas fa-user"></i> Profile of ${sessionScope.username}</h2>
            <hr>
        </div>

        <!-- User Info Cards -->
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card text-white bg-primary">
                    <div class="card-body">
                        <h5><i class="fas fa-envelope"></i> Email</h5>
                        <h2>${sessionScope.email}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-success">
                    <div class="card-body">
                        <h5><i class="fas fa-phone"></i> Phone</h5>
                        <h2>${sessionScope.phone}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-info">
                    <div class="card-body">
                        <h5><i class="fas fa-id-card"></i> Account Type</h5>
                        <h2>${sessionScope.accountType}</h2>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card text-white bg-warning">
                    <div class="card-body">
                        <h5><i class="fas fa-user-check"></i> Status</h5>
                        <h2>${sessionScope.status}</h2>
                    </div>
                </div>
            </div>
           
        </div>

        <!-- Update Form -->
        <div class="card p-4">
            <h4>Update Profile</h4>
            <hr>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/user-profile" method="post">
                <div class="mb-3">
                    <label>Username:</label>
                    <input type="text" class="form-control" value="${sessionScope.username}" readonly>
                </div>

                <div class="mb-3">
                    <label>Email:</label>
                    <input type="email" class="form-control" name="email" value="${sessionScope.email}" required>
                </div>

                <div class="mb-3">
                    <label>Phone:</label>
                    <input type="text" class="form-control" name="phone" value="${sessionScope.phone}">
                </div>

                <div class="mb-3">
                    <label>New Password (leave blank to keep current):</label>
                    <input type="password" class="form-control" name="password">
                </div>

                <button type="submit" class="btn btn-primary">Update Profile</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
