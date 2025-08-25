<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.tss.model.UserEnquiry"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>Arya Vikas Bank - Complaints</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<!-- DataTables CSS -->
<link rel="stylesheet" href="https://cdn.datatables.net/1.13.5/css/dataTables.bootstrap5.min.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.bootstrap5.min.css"/>

<style>
body { background-color: #f8f9fa; }
.sidebar { background-color: #1565c0; min-height: 100vh; }
.sidebar a { color: white; }
.sidebar a:hover { background-color: #0d47a1; }
.form-card { border: 1px solid #dee2e6; border-radius: 8px; }
.tab-content { margin-top: 20px; }
.table th, .table td { vertical-align: middle; }
.status-badge { font-size: 0.85rem; padding: 0.35em 0.6em; }
</style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="user-sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-grow-1 p-4">
        <h2><i class="fas fa-comment-alt"></i> Complaints / Enquiries</h2>
        <hr>

        <!-- Tabs -->
        <ul class="nav nav-tabs" id="complaintTabs" role="tablist">
            <li class="nav-item">
                <a class="nav-link active" id="register-tab" data-bs-toggle="tab" href="#register" role="tab">Register New Complaint</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="view-tab" data-bs-toggle="tab" href="#view" role="tab">View Your Requests</a>
            </li>
        </ul>

        <div class="tab-content">

            <!-- Register New Complaint -->
            <div class="tab-pane fade show active" id="register" role="tabpanel">
                <div class="card shadow-sm form-card p-4 mt-3">

                    <!-- Success / Error Messages -->
                    <c:if test="${not empty sessionScope.successMsg}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            ${sessionScope.successMsg}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="successMsg" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.errorMsg}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${sessionScope.errorMsg}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="errorMsg" scope="session"/>
                    </c:if>

                    <!-- Complaint Form -->
                    <form action="${pageContext.request.contextPath}/user/complaints" method="post">
                        <div class="mb-3">
                            <label for="queryType" class="form-label">Complaint Type</label>
                            <select class="form-select" id="queryType" name="queryType" required>
                                <option value="" disabled selected>Select one</option>
                                <option value="ACCOUNT">ACCOUNT</option>
                                <option value="FD">FD</option>
                                <option value="LOAN">LOAN</option>
                                <option value="TRANSACTION">TRANSACTION</option>
                                <option value="OTHER">OTHER</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="4" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary"><i class="fas fa-paper-plane"></i> Submit Complaint</button>
                    </form>
                </div>
            </div>

            <!-- View Previous Requests -->
            <div class="tab-pane fade" id="view" role="tabpanel">
                <div class="card shadow-sm form-card p-3 mt-3">
                    <table id="complaintsTable" class="table table-bordered table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Type</th>
                                <th>Description</th>
                                <th>Status</th>
                                <th>Submitted At</th>
                                <th>Resolved At</th>
                                <th>Admin Response</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty complaints}">
                                    <c:forEach var="c" items="${complaints}">
                                        <tr>
                                            <td>${c.enquiryId}</td>
                                            <td>${c.queryType}</td>
                                            <td>${c.description}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${c.status == 'PENDING'}">
                                                        <span class="badge bg-warning text-dark status-badge">${c.status}</span>
                                                    </c:when>
                                                    <c:when test="${c.status == 'IN_PROGRESS'}">
                                                        <span class="badge bg-primary status-badge">${c.status}</span>
                                                    </c:when>
                                                    <c:when test="${c.status == 'RESOLVED'}">
                                                        <span class="badge bg-success status-badge">${c.status}</span>
                                                    </c:when>
                                                    <c:when test="${c.status == 'CLOSED'}">
                                                        <span class="badge bg-secondary status-badge">${c.status}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-light text-dark status-badge">${c.status}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${c.submittedAt}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty c.resolvedAt}">${c.resolvedAt}</c:when>
                                                    <c:otherwise>-</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty c.adminResponse}">${c.adminResponse}</c:when>
                                                    <c:otherwise>-</c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="text-center">No complaints found.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.datatables.net/1.13.5/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.5/js/dataTables.bootstrap5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.bootstrap5.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.html5.min.js"></script>

<script>
$(document).ready(function () {
    $('#complaintsTable').DataTable({
        dom: '<"d-flex justify-content-between mb-2"Bf>rt<"d-flex justify-content-between mt-2"lip>',
        buttons: [
            { extend: 'excelHtml5', text: '<i class="fas fa-file-excel"></i> Excel', className: 'btn btn-success' },
            { extend: 'pdfHtml5', text: '<i class="fas fa-file-pdf"></i> PDF', className: 'btn btn-danger' }
        ],
        pageLength: 5,
        lengthMenu: [5, 10, 25, 50, 100]
    });
});
</script>
</body>
</html>
