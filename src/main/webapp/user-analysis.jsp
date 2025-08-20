<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Arya Vikas Bank - Analysis</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body { background-color: #f8f9fa; }
        .sidebar { background-color: #1565c0; min-height: 100vh; width: 250px; position: fixed; }
        .main { margin-left: 250px; padding: 20px; }
        .sidebar a { color: white; text-decoration: none; display: block; padding: 10px; border-radius: 5px; margin: 5px 10px; }
        .sidebar a:hover { background-color: #0d47a1; }
        .sidebar .active { background-color: #0b3c91; }
    </style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar Include -->
    <jsp:include page="user-sidebar.jsp" />

    <!-- Main Content -->
    <div class="main flex-grow-1">
        <h2>Monthly Analysis</h2>
        <hr>

        <!-- Filters -->
        <div class="mb-4">
            <form class="row g-2" method="get">
                <div class="col-md-3">
                    <input type="month" name="month" class="form-control" value="${param.month}" />
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-primary w-100">Apply Filters</button>
                </div>
            </form>
        </div>

        <!-- Charts Section -->
        <div class="row">
            <div class="col-md-6 mb-4">
                <canvas id="creditBarChart"></canvas>
            </div>
            <div class="col-md-6 mb-4">
                <canvas id="debitBarChart"></canvas>
            </div>
            <div class="col-md-6 mb-4">
                <canvas id="creditLineChart"></canvas>
            </div>
            <div class="col-md-6 mb-4">
                <canvas id="debitLineChart"></canvas>
            </div>
        </div>
    </div>
</div>

<script>
const creditLabels = [<c:forEach var="m" items="${creditData.keySet()}">'${m}',</c:forEach>];
const creditValues = [<c:forEach var="m" items="${creditData.keySet()}">${creditData[m]},</c:forEach>];

const debitLabels = [<c:forEach var="m" items="${debitData.keySet()}">'${m}',</c:forEach>];
const debitValues = [<c:forEach var="m" items="${debitData.keySet()}">${debitData[m]},</c:forEach>];

// --- BAR CHARTS ---
new Chart(document.getElementById('creditBarChart'), {
    type: 'bar',
    data: { labels: creditLabels, datasets: [{ label: 'Credits (₹)', data: creditValues, backgroundColor: 'rgba(40, 167, 69, 0.7)' }] },
    options: { responsive: true }
});

new Chart(document.getElementById('debitBarChart'), {
    type: 'bar',
    data: { labels: debitLabels, datasets: [{ label: 'Debits (₹)', data: debitValues, backgroundColor: 'rgba(220, 53, 69, 0.7)' }] },
    options: { responsive: true }
});

// --- LINE CHARTS ---
new Chart(document.getElementById('creditLineChart'), {
    type: 'line',
    data: { labels: creditLabels, datasets: [{ label: 'Credits (₹)', data: creditValues, borderColor: 'green', fill: false, tension: 0.3 }] },
    options: { responsive: true }
});

new Chart(document.getElementById('debitLineChart'), {
    type: 'line',
    data: { labels: debitLabels, datasets: [{ label: 'Debits (₹)', data: debitValues, borderColor: 'red', fill: false, tension: 0.3 }] },
    options: { responsive: true }
});
</script>
</body>
</html>
