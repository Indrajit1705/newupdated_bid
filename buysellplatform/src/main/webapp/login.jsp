<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>Login</h2>
        <form action="auth" method="post">
            <input type="hidden" name="action" value="login">
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <button type="submit">Login</button>
            
            <p class="error-message">
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <%= request.getAttribute("errorMessage") %>
                <% } %>
            </p>
            
            <p>Don't have an account? <a href="signup.jsp">Sign up</a></p>
        </form>
    </div>
</body>
</html>
