package com.buysellplatform.dao;

import com.buysellplatform.model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/buyselldb";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "root";

    // Insert a product into the database
    public boolean listProduct(Product product) {
        String INSERT_PRODUCT_SQL = "INSERT INTO products (title, image_url, description, min_bid_price, current_bid_price, auction_end_date, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        System.out.println("Attempting to insert product into the database...");
        System.out.println("Product Details:");
        System.out.println("Title: " + product.getTitle());
        System.out.println("Image URL: " + product.getImage());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Min Bid Price: " + product.getMinBidPrice());
        System.out.println("Current Bid Price: " + product.getCurrentBidPrice());
        System.out.println("Auction End Date: " + product.getAuctionEndDate()); // Debugging line
        System.out.println("Seller ID: " + product.getSellerId());

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {

                preparedStatement.setString(1, product.getTitle());
                preparedStatement.setString(2, product.getImage());
                preparedStatement.setString(3, product.getDescription());
                preparedStatement.setDouble(4, product.getMinBidPrice());
                preparedStatement.setDouble(5, product.getCurrentBidPrice());
                
                // Use Timestamp directly
                preparedStatement.setTimestamp(6, product.getAuctionEndDate());
                System.out.println("SQL Timestamp: " + product.getAuctionEndDate());

                preparedStatement.setInt(7, product.getSellerId());

                System.out.println("Executing SQL: " + preparedStatement.toString());

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("ProductDAO: Rows affected - " + rowsAffected);
                if (rowsAffected > 0) {
                    System.out.println("Product inserted successfully.");
                } else {
                    System.out.println("Product insertion failed.");
                }
                return rowsAffected > 0;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("ProductDAO: Class not found exception - " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ProductDAO: SQL exception - " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Retrieve a product by its ID
    public Product getProductDetails(int productId) {
        String SELECT_PRODUCT_SQL = "SELECT * FROM products WHERE id = ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_SQL)) {

                preparedStatement.setInt(1, productId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setTitle(resultSet.getString("title"));
                    product.setImage(resultSet.getString("image_url"));
                    product.setDescription(resultSet.getString("description"));
                    product.setMinBidPrice(resultSet.getDouble("min_bid_price"));
                    product.setCurrentBidPrice(resultSet.getDouble("current_bid_price"));
                    
                    // Retrieve and set Timestamp
                    Timestamp auctionEndDate = resultSet.getTimestamp("auction_end_date");
                    product.setAuctionEndDate(auctionEndDate);

                    product.setSellerId(resultSet.getInt("seller_id"));
                    return product;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update the current bid price of a product
    public boolean updateCurrentBidPrice(Product product) {
        String UPDATE_BID_PRICE_SQL = "UPDATE products SET current_bid_price = ? WHERE id = ? AND current_bid_price < ?";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BID_PRICE_SQL)) {

                preparedStatement.setDouble(1, product.getCurrentBidPrice());
                preparedStatement.setInt(2, product.getId());
                preparedStatement.setDouble(3, product.getCurrentBidPrice()); // Ensure new bid is higher

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);

                if (rowsAffected == 0) {
                    System.out.println("No product found with ID: " + product.getId() + " or bid was not higher.");
                }

                return rowsAffected > 0;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Retrieve all products from the database
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("Executing query: " + query);
            
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setTitle(rs.getString("title"));
                product.setCurrentBidPrice(rs.getDouble("current_bid_price"));

                // Debugging: Output product details
                System.out.println("Product retrieved: " + product.getTitle() + " with ID " + product.getId() + " and current bid " + product.getCurrentBidPrice());

                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("SQL exception in getAllProducts(): " + e.getMessage());
            e.printStackTrace();
        }
        
        // Debugging: Output number of products retrieved
        System.out.println("Total products retrieved: " + products.size());
        return products;
    }
}
