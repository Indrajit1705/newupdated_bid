package com.buysellplatform;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {

    private static Connection connection = null;

    static {
        try (InputStream input = DatabaseUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find db.properties");
            }

            Properties prop = new Properties();
            prop.load(input);

            String url = prop.getProperty("db.url");
            String username = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");
            String driver = prop.getProperty("db.driver");

            if (driver != null && !driver.isEmpty()) {
                Class.forName(driver);
            }

            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Database connection is not initialized");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
