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
        PreparedStatement stmt = con.prepareStatement(query)){

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getPhone());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getAddress());

            return stmt.executeUpdate() > 0;

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
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){

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
            PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1,supplier.getName());
            stmt.setString(2,supplier.getEmail());
            stmt.setString(3,supplier.getPhone());
            stmt.setString(4,supplier.getAddress());
            stmt.setInt(5,supplier.getId());

            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id){
        String query = "DELETE FROM Supplier WHERE supplierid = ? ";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement stmt = con.prepareStatement(query)){
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
