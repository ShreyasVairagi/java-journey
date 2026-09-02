package warehouse.dao;

import warehouse.managers.DatabaseManager;
import warehouse.model.InventoryStock;
import warehouse.model.Product;
import warehouse.model.StorageLocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryStockDAO {

    public boolean addProductToLocation(int productId, String locationId, int quantity) {
        String sql = "INSERT INTO inventory_stock (product_id, locationid, quantity) VALUES (?, ?, ?) " +
                "ON CONFLICT (product_id, locationid) DO UPDATE SET quantity = inventory_stock.quantity + ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            pstmt.setString(2, locationId);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, quantity);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding product to location: " + e.getMessage());
            return false;
        }
    }

    public boolean updateQuantity(int productId, String locationId, int quantityChange) {
        String sql = "UPDATE product_location SET quantity = quantity + ? WHERE product_id = ? AND locationid = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantityChange);
            pstmt.setInt(2, productId);
            pstmt.setString(3, locationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalInventoryValue() {
        String query = "SELECT SUM(pl.quantity * p.buyprice) AS total_value " +
                "FROM product_location pl " +
                "JOIN product p ON pl.product_id = p.productid";

        double totalValue = 0.0;

        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                totalValue = rs.getDouble("total_value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalValue;
    }

    public List<InventoryStock> findStockForProduct(Product product) {
        String query = "SELECT pl.locationid, pl.quantity, s.aisle, s.shelf, s.bin, s.capacity " +
                "FROM product_location pl " +
                "JOIN storage_location s ON pl.locationid = s.locationid " +
                "WHERE pl.product_id = ?";

        List<InventoryStock> stockList = new ArrayList<>();

        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, product.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String locationId = rs.getString("locationid");
                    int aisle = rs.getInt("aisle");
                    char shelf = rs.getString("shelf").charAt(0);
                    int bin = rs.getInt("bin");
                    int capacity = rs.getInt("capacity");

                    StorageLocation location = new StorageLocation(locationId, aisle, shelf, bin, capacity);

                    int quantity = rs.getInt("quantity");

                    InventoryStock stockRecord = new InventoryStock(product, location, quantity);
                    stockList.add(stockRecord);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockList;
    }

    public boolean removeLocation(int productId, String locationId) {
        String sql = "DELETE FROM product_location WHERE product_id = ? AND locationid = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            pstmt.setString(2, locationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeLocation(int productId) {
        String sql = "DELETE FROM product_location WHERE product_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean locationExists(String locationId) {
        String sql = "SELECT 1 FROM storage_location WHERE locationid = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, locationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error checking location existence: " + e.getMessage());
            return false;
        }
    }


}
