package warehouse.dao;

import warehouse.managers.DatabaseManager;
import warehouse.model.*;

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
        String sql = "INSERT INTO product (name, description, buyprice, sellprice, minimumstock, supplierid) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getBuyPrice());
            pstmt.setDouble(4, product.getSellPrice());
            pstmt.setInt(5, product.getMinimumStock());
            pstmt.setInt(6, product.getSupplier().getId());

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
                Supplier supplier = new Supplier(rs.getInt("supplierid"));

                Product product = new Product(
                        rs.getInt("productid"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("buyprice"),
                        rs.getDouble("sellprice"),
                        rs.getInt("minimumstock"),
                        supplier
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product findSingleProduct(int id) {
        String query = "SELECT * FROM product WHERE productid = ?";
        Product product = null;

        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int empId = rs.getInt("productid");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    Double buyprice =  rs.getDouble("buyprice");
                    Double sellprice =  rs.getDouble("sellprice");
                    int minimumstock =  rs.getInt("minimumstock");
                    Supplier supplier = new Supplier(rs.getInt("supplierid"));

                    product = new Product(empId, name, description, buyprice, sellprice, minimumstock, supplier); // Assign here
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    //update
    public boolean updateProduct(Product product) {
        // 7 placeholders total
        String sql = "UPDATE product SET name = ?, description = ?, buyprice = ?, sellprice = ?, minimumstock = ?, supplierid = ? WHERE productid = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setDouble(3, product.getBuyPrice());
            pstmt.setDouble(4, product.getSellPrice());
            pstmt.setInt(5, product.getMinimumStock());
            pstmt.setInt(6, product.getSupplier().getId());
            pstmt.setInt(7, product.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    //delete
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE productid = ?";

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
