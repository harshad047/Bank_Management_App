<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.tss.model.User"%>
<%
    List<User> users = (List<User>) request.getAttribute("users");
    String searchQuery = request.getParameter("search") != null ? request.getParameter("search") : "";

    // ✅ Pagination settings
    int recordsPerPage = 5;
    int currentPage = 1;
    if (request.getParameter("page") != null) {
        try {
            currentPage = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
    }

    int totalRecords = (users != null) ? users.size() : 0;
    int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

    int startIndex = (currentPage - 1) * recordsPerPage;
    int endIndex = Math.min(startIndex + recordsPerPage, totalRecords);
%>  
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<title>Arya Vikas Bank - Manage Users</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

<style>
body { background-color: #f4f6f9; }
.sidebar { background: #1a2537; min-height: 100vh; }
.sidebar a { color: #dfe4ea; }
.sidebar a:hover { background: #2f3b52; }
</style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="admin-sidebar.jsp" />

    <!-- Main Content -->
    <div class="flex-grow-1 p-4">
        <h2><i class="fas fa-users"></i> Manage Users</h2>
        <hr>

        <!-- Search Bar -->
        <form method="get" class="row mb-4">
            <div class="col-md-8">
                <input type="text" name="search" class="form-control"
                    placeholder="Search by username, email, or account number..."
                    value="<%=searchQuery%>" />
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Search</button>
            </div>
            <div class="col-md-2">
                <a href="manage-users" class="btn btn-secondary w-100">Reset</a>
            </div>
        </form>

        <!-- Success/Error Messages -->
        <%
            if (request.getAttribute("success") != null) {
        %>
            <div class="alert alert-success"><%=request.getAttribute("success")%></div>
            <%} %>
            
        
		
        <!-- Export Buttons -->
        <div class="mb-3 d-flex justify-content-end gap-2">
            <button class="btn btn-danger btn-sm" onclick="downloadPDF()">
                <i class="fas fa-file-pdf"></i> Download PDF
            </button>
            <button class="btn btn-success btn-sm" onclick="downloadExcel()">
                <i class="fas fa-file-excel"></i> Download Excel
            </button>
        </div>

        <!-- Users Table -->
        <div class="table-responsive">
            <table id="usersTable" class="table table-bordered table-hover align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Account Type</th>
                        <th>Status</th>
                        <th class="text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    if (users == null || users.isEmpty()) {
                    %>
                    <tr>
                        <td colspan="7" class="text-center text-muted">No users found.</td>
                    </tr>
                    <%
                    } else {
                        for (int i = startIndex; i < endIndex; i++) {
                            User u = users.get(i);
                            String status = u.getStatus() != null ? u.getStatus() : "UNKNOWN";
                            String badgeClass = "PENDING".equals(status) ? "bg-warning text-dark"
                                : "APPROVED".equals(status) ? "bg-success" 
                                : "REJECTED".equals(status) ? "bg-danger" 
                                : "bg-secondary";
                    %>
                    <tr>
                        <td><%=u.getUserId()%></td>
                        <td><%=u.getUsername()%></td>
                        <td><%=u.getEmail()%></td>
                        <td><%=u.getPhone() != null ? u.getPhone() : "N/A"%></td>
                        <td><%=u.getAccountType()%></td>
                        <td><span class="badge <%=badgeClass%>"><%=status%></span></td>
                        <td class="text-center">
                            <% if ("PENDING".equals(status)) { %>
                                <span class="text-muted">Approval Pending</span>
                            <% } else { %>
                                <button type="button" class="btn btn-warning btn-sm"
                                    data-bs-toggle="modal" data-bs-target="#editModal<%=u.getUserId()%>">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button type="button" class="btn btn-danger btn-sm"
                                    data-bs-toggle="modal" data-bs-target="#deleteModal<%=u.getUserId()%>">
                                    <i class="fas fa-trash"></i>
                                </button>
                            <% } %>
                        </td>
                    </tr>

                    <!-- Edit Modal -->
                    <div class="modal fade" id="editModal<%=u.getUserId()%>" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="manage-users" method="post">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Edit User Status: <%=u.getUsername()%></h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <input type="hidden" name="userId" value="<%=u.getUserId()%>" />
                                        <input type="hidden" name="action" value="edit" />
                                        <div class="mb-3">
                                            <label class="form-label">New Status</label>
                                            <select class="form-select" name="newStatus" required>
                                                <option value="">-- Select Status --</option>
                                                <%
                                                if ("REJECTED".equals(status) || "DEACTIVATED".equals(status)) {
                                                %>
                                                <option value="APPROVED">Activate</option>
                                                <%
                                                } 
                                                %>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-warning">Update</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Delete Modal -->
                    <div class="modal fade" id="deleteModal<%=u.getUserId()%>" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="manage-users" method="post">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Delete User: <%=u.getUsername()%></h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <input type="hidden" name="userId" value="<%=u.getUserId()%>" />
                                        <input type="hidden" name="action" value="delete" />
                                        <p>Are you sure you want to delete this user? This action cannot be undone.</p>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-danger">Delete</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <% } } %>
                </tbody>
            </table>
        </div>

        <!-- ✅ Pagination Controls -->
        <nav>
            <ul class="pagination justify-content-center">
                <li class="page-item <%= (currentPage == 1) ? "disabled" : "" %>">
                    <a class="page-link" href="?page=<%=currentPage - 1%>&search=<%=searchQuery%>">Previous</a>
                </li>
                <% for (int i = 1; i <= totalPages; i++) { %>
                    <li class="page-item <%= (i == currentPage) ? "active" : "" %>">
                        <a class="page-link" href="?page=<%=i%>&search=<%=searchQuery%>"><%=i%></a>
                    </li>
                <% } %>
                <li class="page-item <%= (currentPage == totalPages) ? "disabled" : "" %>">
                    <a class="page-link" href="?page=<%=currentPage + 1%>&search=<%=searchQuery%>">Next</a>
                </li>
            </ul>
        </nav>
    </div>
</div>

<!-- Export Libraries -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.25/jspdf.plugin.autotable.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>

<script>
function downloadPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    doc.text("Manage Users Report", 14, 10);
    doc.autoTable({ html: "#usersTable", startY: 20 });
    doc.save("users-report.pdf");
}

function downloadExcel() {
    var table = document.getElementById("usersTable");
    var wb = XLSX.utils.table_to_book(table, { sheet: "Users" });
    XLSX.writeFile(wb, "users-report.xlsx");
}
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
