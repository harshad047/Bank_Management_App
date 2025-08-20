<!-- webapp/register/step2.jsp -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tss.model.SecurityQuestion" %>
<%
    List<SecurityQuestion> questions = (List<SecurityQuestion>) request.getAttribute("questions");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Arya Vikas Bank - Register Step 2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body {
            background: linear-gradient(135deg, #fff3e0, #ffcc80);
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
        <div class="col-md-8">
            <div class="card step-card">
                <div class="card-header bg-warning text-dark text-center">
                    <h4><i class="fas fa-user-edit"></i> Complete Your Registration</h4>
                    <small>Step 2: Personal & Security Details</small>
                </div>
                <div class="card-body">
                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <form action="step2" method="post">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username" required />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" required />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="phone" class="form-label">Phone (10 digits)</label>
                                <input type="text" class="form-control" id="phone" name="phone" pattern="[0-9]{10}" required />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" name="password" minlength="6" required />
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="questionId" class="form-label">Security Question</label>
                            <select class="form-select" id="questionId" name="questionId" required>
                                <option value="">-- Select One --</option>
                                <% for (SecurityQuestion q : questions) { %>
                                <option value="<%= q.getQuestionId() %>"><%= q.getQuestionText() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="answer" class="form-label">Answer</label>
                            <input type="text" class="form-control" id="answer" name="answer" required />
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-between">
                            <a href="step1" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Back</a>
                            <button type="submit" class="btn btn-warning text-dark">Register <i class="fas fa-user-check"></i></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>