package warehouse.dao;

import warehouse.DatabaseManager;
import warehouse.model.Employee;
import warehouse.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionDAO {
    //CRUD

    //add
//    public boolean add(Transaction transaction){
//        String query = "INSERT INTO Transaction (name, email, phone, address , role) VALUES (?, ?, ?, ?, ?);";
//        try(Connection con = DatabaseManager.connect();
//            PreparedStatement stmt = con.prepareStatement(query)){
//
//            stmt.setString(1, transaction.getName());
//            stmt.setString(2, transaction.getEmail());
//            stmt.setString(3, transaction.getPhone());
//            stmt.setString(4, transaction.getAddress());
//            stmt.setString(5, transaction.getRole().name());
//
//            return stmt.executeUpdate() > 0;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
    //Read all
    //update
    //delete
}
