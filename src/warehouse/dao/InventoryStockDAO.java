package warehouse.dao;

import warehouse.DatabaseManager;
import warehouse.model.InventoryStock;
import warehouse.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryStockDAO {

    public boolean updateQuantity(InventoryStock stockRecord) {
        String sql = "UPDATE product_location SET quantity = quantity + ? WHERE product_id = ? AND locationid = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockRecord.getQuantity());
            pstmt.setInt(2, stockRecord.getProduct().getId());
            pstmt.setString(3, stockRecord.getLocation().getLocationID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
