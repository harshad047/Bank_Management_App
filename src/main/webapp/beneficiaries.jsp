<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tss.model.Account, com.tss.model.Beneficiary" %>

<!DOCTYPE html>
<html>
<head>
    <title>Manage Beneficiaries</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="p-4">

<h3>Manage Beneficiaries</h3>
<hr/>

<!-- Add Beneficiary Form -->
<form action="${pageContext.request.contextPath}/user/beneficiaries" method="post" class="mb-4">
    <input type="hidden" name="action" value="add"/>

    <div class="row mb-3">
        <div class="col-md-6">
            <label for="beneficiaryAccId" class="form-label">Select Beneficiary Account</label>
            <select name="beneficiaryAccId" id="beneficiaryAccId" class="form-select" required>
                <option value="">-- Select Account --</option>
                <%
                    List<Account> accounts = (List<Account>) request.getAttribute("accounts");
                    if (accounts != null) {
                        for (Account acc : accounts) {
                %>
                    <option value="<%= acc.getAccountId() %>">
                        <%= acc.getAccountNumber() %> (Balance: <%= acc.getBalance() %>)
                    </option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <div class="col-md-6">
            <label for="nickname" class="form-label">Nickname</label>
            <input type="text" name="nickname" id="nickname" class="form-control" required/>
        </div>
    </div>

    <button type="submit" class="btn btn-primary">Add Beneficiary</button>
</form>

<hr/>

<!-- List of Beneficiaries -->
<h4>Existing Beneficiaries</h4>
<table class="table table-bordered">
    <thead class="table-light">
        <tr>
            <th>ID</th>
            <th>Nickname</th>
            <th>Account Number</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
    <%
        List<Beneficiary> beneficiaries = (List<Beneficiary>) request.getAttribute("beneficiaries");
        if (beneficiaries != null && !beneficiaries.isEmpty()) {
            for (Beneficiary b : beneficiaries) {
    %>
        <tr>
            <td><%= b.getBeneficiaryId() %></td>
            <td><%= b.getNickname() %></td>
            <td><%= b.getBeneficiaryAccId() %></td>
            <td>
                <form action="${pageContext.request.contextPath}/user/beneficiaries" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="remove"/>
                    <input type="hidden" name="beneficiaryId" value="<%= b.getBeneficiaryId() %>"/>
                    <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                </form>
            </td>
        </tr>
    <%
            }
        } else {
    %>
        <tr><td colspan="4" class="text-center">No beneficiaries found</td></tr>
    <%
        }
    %>
    </tbody>
</table>

</body>
</html>
