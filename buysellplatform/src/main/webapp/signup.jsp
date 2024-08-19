<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign Up</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>Sign Up</h2>
        <form action="auth" method="post">
            <input type="hidden" name="action" value="register">
            
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label for="college">College:</label>
                <input type="text" id="college" name="college" required>
            </div>
            
            <div class="form-group">
                <label for="whatsappNumber">WhatsApp Number:</label>
                <input type="text" id="whatsappNumber" name="whatsappNumber" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <button type="submit">Sign Up</button>
            
            <p class="error-message">
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <%= request.getAttribute("errorMessage") %>
                <% } %>
            </p>
            
            <p>Already have an account? <a href="login.jsp">Login</a></p>
        </form>
    </div>
</body>
</html>
