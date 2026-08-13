package warehouse;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/warehouse_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Java123";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}