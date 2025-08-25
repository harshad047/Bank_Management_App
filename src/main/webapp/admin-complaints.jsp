<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.tss.model.UserEnquiry"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>Arya Vikas Bank - Admin Complaints</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<!-- DataTables CSS -->
<link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.bootstrap5.min.css">

<style>
body { background-color: #f8f9fa; }
.sidebar { background-color: #1565c0; min-height: 100vh; }
.sidebar a { color: white; }
.sidebar a:hover { background-color: #0d47a1; }
.card { border-radius: 8px; }
.status-badge { font-size: 0.85rem; padding: 0.35em 0.6em; }
</style>
</head>
<body>
<div class="d-flex">
    <!-- Admin Sidebar -->
    <jsp:include page="admin-sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-grow-1 p-4">
        <h2><i class="fas fa-tasks"></i> Manage Complaints</h2>
        <hr>

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

        <!-- Complaints Table -->
        <div class="card shadow-sm p-3">
            <div class="table-responsive">
                <table id="complaintsTable" class="table table-bordered table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>User</th>
                            <th>Type</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th>Submitted At</th>
                            <th>Resolved At</th>
                            <th>Admin Response</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty complaints}">
                                <c:forEach var="c" items="${complaints}">
                                    <tr>
                                        <td>${c.enquiryId}</td>
                                        <td>${c.user.username}</td>
                                        <td>${c.queryType}</td>
                                        <td>${c.description}</td>
                                        <!-- Status Badge -->
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
                                        <td><c:choose>
                                                <c:when test="${not empty c.resolvedAt}">${c.resolvedAt}</c:when>
                                                <c:otherwise>-</c:otherwise>
                                            </c:choose></td>
                                        <td>${c.adminResponse != null ? c.adminResponse : '-'}</td>

                                        <!-- Admin Action Form -->
                                        <td>
                                            <form action="${pageContext.request.contextPath}/admin/complaints" method="post" class="d-flex flex-column gap-1">
                                                <input type="hidden" name="enquiryId" value="${c.enquiryId}" />
                                                <select class="form-select form-select-sm" name="status" required
                                                    <c:if test="${c.status == 'CLOSED'}">disabled</c:if>>
                                                    <option value="" disabled>Select Status</option>
                                                    <option value="PENDING" ${c.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                                                    <option value="IN_PROGRESS" ${c.status == 'IN_PROGRESS' ? 'selected' : ''}>IN_PROGRESS</option>
                                                    <option value="RESOLVED" ${c.status == 'RESOLVED' ? 'selected' : ''}>RESOLVED</option>
                                                    <option value="CLOSED" ${c.status == 'CLOSED' ? 'selected' : ''}>CLOSED</option>
                                                </select>
                                                <input type="text" name="adminResponse" class="form-control form-control-sm" placeholder="Admin Response"
                                                    value="${c.adminResponse != null ? c.adminResponse : ''}" 
                                                    <c:if test="${c.status == 'CLOSED'}">disabled</c:if> />
                                                <button type="submit" class="btn btn-sm btn-success mt-1"
                                                    <c:if test="${c.status == 'CLOSED'}">disabled</c:if>>
                                                    <i class="fas fa-save"></i> Update
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="9" class="text-center">No complaints found.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- jQuery + DataTables -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

<!-- Export Buttons -->
<script src="https://cdn.datatables.net/buttons/2.4.1/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.bootstrap5.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.html5.min.js"></script>

<script>
    $(document).ready(function () {
        $('#complaintsTable').DataTable({
            "pageLength": 5,
            "lengthMenu": [10, 25, 50, 100],
            "ordering": true,
            "searching": true,
            "language": {
                "search": "Search Complaints:"
            },
            dom: 'Bfrtip',
            buttons: [
                {
                    extend: 'excelHtml5',
                    text: '<i class="fas fa-file-excel"></i> Export Excel',
                    className: 'btn btn-success btn-sm'
                },
                {
                    extend: 'pdfHtml5',
                    text: '<i class="fas fa-file-pdf"></i> Export PDF',
                    className: 'btn btn-danger btn-sm',
                    orientation: 'landscape',
                    pageSize: 'A4',
                    exportOptions: {
                        columns: ':visible:not(:last-child)' // exclude Action column
                    }
                }
            ]
        });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
