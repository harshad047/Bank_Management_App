<!-- webapp/register/step1.jsp -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Arya Vikas Bank - Register Step 1</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body {
            background: linear-gradient(135deg, #e8f5e8, #c8e6c9);
            font-family: 'Segoe UI', sans-serif;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .step-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card step-card">
                <div class="card-header bg-success text-white text-center">
                    <h4><i class="fas fa-user-plus"></i> Create New Account</h4>
                    <small>Step 1: Select Account Type</small>
                </div>
                <div class="card-body">
                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <form action="step1" method="post">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <div class="form-check h-100">
                                    <input class="form-check-input" type="radio" name="accountType" value="SAVINGS" id="savings" required>
                                    <label class="form-check-label" for="savings">
                                        <div class="p-3 border rounded text-center h-100">
                                            <i class="fas fa-piggy-bank fa-2x text-success mb-2"></i>
                                            <h5>Savings Account</h5>
                                            <small>For personal savings and regular banking</small>
                                        </div>
                                    </label>
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <div class="form-check h-100">
                                    <input class="form-check-input" type="radio" name="accountType" value="CURRENT" id="current" required>
                                    <label class="form-check-label" for="current">
                                        <div class="p-3 border rounded text-center h-100">
                                            <i class="fas fa-building fa-2x text-info mb-2"></i>
                                            <h5>Current Account</h5>
                                            <small>For business and high-volume transactions</small>
                                        </div>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-success w-100">
                            Continue <i class="fas fa-arrow-right"></i>
                        </button>
                    </form>
                    <div class="text-center mt-3">
                        <a href="../login" class="text-decoration-none">Back to Login</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>