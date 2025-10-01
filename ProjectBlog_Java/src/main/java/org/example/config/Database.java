package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static String url = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:sqlserver://localhost:1433;databaseName=BlogDB;encrypt=true;trustServerCertificate=true"
    );
    private static String username = System.getenv().getOrDefault("DB_USER", "sa");
    private static String password = System.getenv().getOrDefault("DB_PASS", "123");

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void configure(String jdbcUrl, String user, String pass) {
        url = jdbcUrl;
        username = user;
        password = pass;
    }
    
    public static void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

