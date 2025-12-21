package com.wipro.eshop.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHandler {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/connection.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            properties.getProperty("connection-url"),
            properties.getProperty("username"),
            properties.getProperty("password"));
    }
}