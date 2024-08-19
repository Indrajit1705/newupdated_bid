package com.buysellplatform.dao;

import com.buysellplatform.model.Bid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BidDAO {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/buyselldb";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "root";

    public boolean placeBid(Bid bid) {
        String INSERT_BID_SQL = "INSERT INTO bids (product_id, buyer_id, bid_price) VALUES (?, ?, ?)";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BID_SQL)) {

                preparedStatement.setInt(1, bid.getProductId());
                preparedStatement.setInt(2, bid.getBuyerId());
                preparedStatement.setDouble(3, bid.getBidPrice());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
