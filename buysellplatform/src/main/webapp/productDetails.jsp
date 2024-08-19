<%@ page import="com.buysellplatform.model.Product" %>
<%@ page import="java.math.BigDecimal" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Details</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>Product Details</h2>
        <img src="${product.imageUrl}" alt="${product.title}" width="200px">
        <h3>${product.title}</h3>
        <p>${product.description}</p>
        <p>Minimum Bid Price: $${product.minBidPrice}</p>
        <p>Current Bid Price: $${product.currentBidPrice}</p>
        <form action="product-details" method="post">
            <input type="hidden" name="productId" value="${product.id}">
            <div class="form-group">
                <label for="bidPrice">Enter your bid price:</label>
                <input type="text" name="bidPrice" id="bidPrice" required>
            </div>
            <button type="submit">Submit Bid</button>
        </form>
        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>
    </div>
</body>
</html>
