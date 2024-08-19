<%@ page import="java.util.List" %>
<%@ page import="com.buysellplatform.model.Product" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product List</title>
    <!-- <link rel="stylesheet" type="text/css" href="styles.css"> -->
</head>
<body>
    <h1>Product List</h1>

    <!-- Debugging output -->
    <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (products == null || products.isEmpty()) {
            out.println("No products available.");
        } else {
            for (Product product : products) {
                Timestamp auctionEndDate = product.getAuctionEndDate(); // Assuming this is a Timestamp
                String formattedDate = auctionEndDate != null ? outputDateFormat.format(new Date(auctionEndDate.getTime())) : "Invalid Date";
    %>
    <ul>
        <li>
            <a href="productDetails.jsp?id=<%= product.getId() %>">
                <%= product.getTitle() %> - $<%= product.getCurrentBidPrice() %>
            </a>
            <p>Min Bid Price: $<%= product.getMinBidPrice() %></p>
            <p>Auction End Date: <%= formattedDate %></p>
        </li>
    </ul>
    <%
            }
        }
    %>
    
    <!-- I Want to Sell Button -->
    <form action="sell.jsp" method="get">
        <button type="submit">I want to sell</button>
    </form>
</body>
</html>
