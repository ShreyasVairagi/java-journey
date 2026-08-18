package warehouse.dao;

import warehouse.DatabaseManager;
import warehouse.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    //CRUD

    // Add Supplier
    public boolean add(Supplier supplier){
        String query = "INSERT INTO Supplier (name, phone, email, address) VALUES (?,?,?,?);";
        try(Connection con = DatabaseManager.connect();
        PreparedStatement pstmt = con.prepareStatement(query)){

            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getPhone());
            pstmt.setString(3, supplier.getEmail());
            pstmt.setString(4, supplier.getAddress());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read Supplier data
    public List<Supplier> veiwAll(){
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM Supplier";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()){

            while (rs.next()) {
                Supplier s = new Supplier(rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"));

                suppliers.add(s);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return suppliers;
    }

    //Update Info
    public boolean update(Supplier supplier){
        String query = "UPDATE Supplier SET name = ?, phone = ?, email = ?, address = ? WHERE supplierid = ?;";

        try(Connection con = DatabaseManager.connect();
            PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.setString(1,supplier.getName());
            pstmt.setString(2,supplier.getEmail());
            pstmt.setString(3,supplier.getPhone());
            pstmt.setString(4,supplier.getAddress());
            pstmt.setInt(5,supplier.getId());

            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id){
        String query = "DELETE FROM Supplier WHERE supplierid = ? ";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement pstmt = con.prepareStatement(query)){
                pstmt.setInt(1, id);
                return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
