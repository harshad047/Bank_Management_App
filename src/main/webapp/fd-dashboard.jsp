<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Manage FDs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { background-color: #f4f6f9; }
        .sidebar { background: #1a2537; }
        .btn-option { font-size: 1.2rem; padding: 1.5rem; }
        .card { border-radius: 12px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
    </style>
</head>
<body>
<div class="d-flex">
    <jsp:include page="admin-sidebar.jsp"/>

    <div class="flex-grow-1 p-4">
        <h2><i class="fas fa-certificate"></i> Manage Fixed Deposits</h2>
        <hr>

        <!-- Options -->
        <div class="row mb-5">
            <div class="col-md-4 mb-3">
                <a href="manage-fds?view=approve" class="btn btn-outline-primary w-100 btn-option">
                    <i class="fas fa-check-circle"></i> Approve/Reject FDs
                </a>
            </div>
            <div class="col-md-4 mb-3">
                <a href="manage-fds?view=close" class="btn btn-outline-success w-100 btn-option">
                    <i class="fas fa-door-open"></i> Close FDs
                </a>
            </div>
            <div class="col-md-4 mb-3">
                <a href="manage-fds?view=all" class="btn btn-outline-info w-100 btn-option">
                    <i class="fas fa-list"></i> View All FDs
                </a>
            </div>
        </div>

        <!-- Pie Chart -->
        <div class="row">
            <div class="col-md-6 mx-auto">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h5>FD Status Distribution</h5>
                        <canvas id="fdChart" height="300"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const ctx = document.getElementById('fdChart').getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Pending', 'Approved', 'Closed'],
            datasets: [{
                data: [
                    ${counts.PENDING},
                    ${counts.APPROVED},
                    ${counts.CLOSED}
                ],
                backgroundColor: ['#ffc107', '#28a745', '#6c757d']
            }]
        },
        options: { responsive: true, plugins: { legend: { position: 'bottom' } } }
    });
</script>
</body>
</html>