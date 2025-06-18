package com.movieratingsystem.utils;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class DBUtil {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.username");
            PASSWORD = prop.getProperty("db.password");

            Class.forName(prop.getProperty("db.driver"));
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to load DB config: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void closePreparedStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Error closing prepared statement: " + e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("Error closing result set: " + e.getMessage());
            }
        }
    }

    public static void closeAll(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closePreparedStatement(statement);
        closeConnection(connection);
    }
}
