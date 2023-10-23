package peaksoft.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConfig {
    private static Connection connection;
    public static Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection
                    ("jdbc:postgresql://localhost:5432/postgres",
                            "postgres",
                            "1234");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

