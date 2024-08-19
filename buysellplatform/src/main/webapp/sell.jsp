<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sell a Product</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>List a New Product</h2>
        <form action="sell" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="title">Product Title:</label>
                <input type="text" name="title" id="title" required>
            </div>
            <div class="form-group">
                <label for="image">Upload Image:</label>
                <input type="file" name="image" id="image" required>
            </div>
            <div class="form-group">
                <label for="description">Product Description:</label>
                <textarea name="description" id="description" required></textarea>
            </div>
            <div class="form-group">
                <label for="minBidPrice">Minimum Bid Price:</label>
                <input type="number" name="minBidPrice" id="minBidPrice" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="auctionEndDate">Auction End Date:</label>
                <input type="date" name="auctionEndDate" id="auctionEndDate" required>
            </div>
            <div class="form-group">
                <label for="auctionEndTime">Auction End Time:</label>
                <input type="time" name="auctionEndTime" id="auctionEndTime" required>
            </div>
            <button type="submit">List Product</button>
        </form>
    </div>
</body>
</html>
