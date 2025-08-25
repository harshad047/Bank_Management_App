<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<!-- DataTables CSS -->
<link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.bootstrap5.min.css">

<style>
body {
    background-color: #f8f9fa;
}
.sidebar {
    background-color: #212529;
    min-height: 100vh;
}
.sidebar a {
    color: white;
    display: block;
    padding: 10px;
    text-decoration: none;
}
.sidebar a:hover {
    background-color: #343a40;
}
.card {
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, .15);
}
.status-badge {
    font-size: 0.85rem;
    padding: 0.35em 0.6em;
}
</style>
</head>
<body>
    <div class="d-flex">
        <!-- Sidebar -->
        <jsp:include page="admin-sidebar.jsp" />

        <!-- Main Content -->
        <div class="flex-grow-1 p-4">
            <h2>
                <i class="fas fa-tachometer-alt"></i> Admin Dashboard
            </h2>
            <hr />

            <!-- KPI Cards -->
            <div class="row text-center mb-4">
                <div class="col-md-3">
                    <div class="card bg-primary text-white p-3 rounded-3">
                        <h5>Total Users</h5>
                        <h3>${totalUsers}</h3>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-success text-white p-3 rounded-3">
                        <h5>Approved</h5>
                        <h3>${approvedCount}</h3>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning text-dark p-3 rounded-3">
                        <h5>Pending</h5>
                        <h3>${pendingCount}</h3>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-danger text-white p-3 rounded-3">
                        <h5>Rejected</h5>
                        <h3>${rejectedCount}</h3>
                    </div>
                </div>
            </div>

            <!-- Recent Activity -->
            <div class="card mt-4">
                <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">
                        <i class="fas fa-bell"></i> Recent Activity (Today)
                    </h5>
                    <!-- Filter Dropdown -->
                    <form method="get" action="dashboard" class="d-flex">
                        <select name="filter" class="form-select form-select-sm me-2"
                                onchange="this.form.submit()">
                            <option value="ALL" ${selectedFilter == 'ALL' ? 'selected' : ''}>All</option>
                            <option value="APPROVED" ${selectedFilter == 'APPROVED' ? 'selected' : ''}>Approved</option>
                            <option value="REJECTED" ${selectedFilter == 'REJECTED' ? 'selected' : ''}>Rejected</option>
                            <option value="DEACTIVATED" ${selectedFilter == 'DEACTIVATED' ? 'selected' : ''}>Deactivated</option>
                            <option value="CLOSED" ${selectedFilter == 'CLOSED' ? 'selected' : ''}>Closed</option>
                            <option value="IN_PROGRESS" ${selectedFilter == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                            <option value="PENDING" ${selectedFilter == 'PENDING' ? 'selected' : ''}>Pending</option>
                            <option value="RESOLVED" ${selectedFilter == 'RESOLVED' ? 'selected' : ''}>Resolved</option>
                        </select>
                    </form>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty todayApprovals}">
                            <p class="text-muted">No activity found for today.</p>
                        </c:when>
                        <c:otherwise>
                            <!-- DataTable -->
                            <table id="activityTable" class="table table-striped table-bordered">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Activity ID</th>
                                        <th>User ID</th>
                                        <th>Admin ID</th>
                                        <th>Action</th>
                                        <th>Reason</th>
                                        <th>Time</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="a" items="${todayApprovals}">
                                        <tr>
                                            <td>${a.approvalId}</td>
                                            <td>${a.userId}</td>
                                            <td>${a.adminId}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${a.action == 'APPROVED'}">
                                                        <span class="badge bg-success status-badge">${a.action}</span>
                                                    </c:when>
                                                    <c:when test="${a.action == 'REJECTED'}">
                                                        <span class="badge bg-danger status-badge">${a.action}</span>
                                                    </c:when>
                                                    <c:when test="${a.action == 'DEACTIVATED'}">
                                                        <span class="badge bg-dark status-badge">${a.action}</span>
                                                    </c:when>
                                                    <c:when test="${a.action == 'CLOSED'}">
                                                        <span class="badge bg-secondary status-badge">${a.action}</span>
                                                    </c:when>
                                                    <c:when test="${a.action == 'IN_PROGRESS'}">
                                                        <span class="badge bg-primary status-badge">${a.action}</span>
                                                    </c:when>
                                                    <c:when test="${a.action == 'PENDING'}">
                                                        <span class="badge bg-warning text-dark status-badge">${a.action}</span>
                                                    </c:when>
                                                    <c:when test="${a.action == 'RESOLVED'}">
                                                        <span class="badge bg-info text-dark status-badge">${a.action}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-light text-dark status-badge">${a.action}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${a.reason}</td>
                                            <td>${a.createdAt}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- jQuery + DataTables + Buttons -->
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>


    <script>
        $(document).ready(function () {
            $('#activityTable').DataTable({
                "pageLength": 5,
                "lengthMenu": [5, 10, 25, 50],
                "ordering": true,
                "searching": true,
                "language": {
                    "search": "Filter records:"
                },
                dom: 'Bfrtip',
                buttons: [
                    'copy', 'excel', 'pdf', 'print'
                ]
            });
        });
    </script>
</body>
</html>
