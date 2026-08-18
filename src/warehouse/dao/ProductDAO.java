package warehouse.dao;

import warehouse.DatabaseManager;
import warehouse.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    //CRUD

    //add
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO product (name, description, buyprice, sellprice, quantity, minimumstock, supplierid, locationid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getBuyPrice());
            pstmt.setDouble(4, product.getSellPrice());
            pstmt.setInt(5, product.getQuantity());
            pstmt.setInt(6, product.getMinimumStock());
            pstmt.setInt(7, product.getSupplier());
            pstmt.setString(8, product.getStorageLocationID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Read all
    public List<Product> viewAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("buyprice"),
                        rs.getDouble("sellprice"),
                        rs.getInt("quantity"),
                        rs.getInt("minimumstock"),
                        rs.getInt("supplierid"),
                        rs.getString("locationid")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    //update
    public boolean updateProduct(Product product) {
        String sql = "UPDATE product SET name = ?, description = ?, buyprice = ?, sellprice = ?, quantity = ?, minimumstock = ?, supplier = ?, locationid = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getBuyPrice());
            pstmt.setDouble(4, product.getSellPrice());
            pstmt.setInt(5, product.getQuantity());
            pstmt.setInt(6, product.getMinimumStock());
            pstmt.setInt(7, product.getSupplier());
            pstmt.setString(8, product.getStorageLocationID());
            pstmt.setInt(9, product.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
