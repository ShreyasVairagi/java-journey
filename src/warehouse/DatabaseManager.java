package warehouse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void insertTestSupplier() {
        String sql = "INSERT INTO Supplier (supplierId, name, phone, email, address) " +
                "VALUES (1, 'Shreyas', 74486752, 'Shreyas@gmail.com', '123 sdhgasd asjdb ask');";

        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
                statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}