package com.tss.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/bank_management";
    private static final String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : DEFAULT_URL;
    private static final String USERNAME = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static final String PASSWORD = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private DBConnection() {}
}