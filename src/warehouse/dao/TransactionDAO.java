package warehouse.dao;

import warehouse.DatabaseManager;
import warehouse.model.Transaction;
import warehouse.model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    //CRUD

    //add
    public boolean add(Transaction transaction){
        String query = "INSERT INTO Transaction (productid, employeeid, type, quantity , date, time) VALUES (?, ?, ?, ?, ?, ?);";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement pstmt = con.prepareStatement(query)){

            pstmt.setInt(1, transaction.getProduct());
            pstmt.setInt(2, transaction.getEmployee());
            pstmt.setInt(3, transaction.getQuantity());
            pstmt.setString(4, transaction.getType().name());
            pstmt.setInt(5, transaction.getDate());
            pstmt.setInt(6, transaction.getTime());

            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    //Read all
    public List<Transaction> veiwAllTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transaction";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getInt("transactionid"),
                        rs.getInt("productid"),
                        rs.getInt("employeeid"),
                        rs.getInt("quantity"),
                        TransactionType.valueOf(rs.getString("type").toUpperCase()), // type is 5th
                        rs.getInt("date"),
                        rs.getInt("time")
                );

                transactions.add(t);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return transactions;
    }

    //update
    public boolean update(Transaction transaction){
        String query = "UPDATE Transaction SET productid = ?, employeeid = ?, quantity = ?, type = ?,date = ?, time = ?   WHERE transactionid = ?;";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.setInt(1, transaction.getProduct());
            pstmt.setInt(2, transaction.getEmployee());
            pstmt.setInt(3, transaction.getQuantity());
            pstmt.setString(5, transaction.getType().name());
            pstmt.setInt(4, transaction.getDate());
            pstmt.setInt(4, transaction.getTime());
            pstmt.setInt(6,transaction.getId());

            return pstmt.executeUpdate() > 0;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    //delete
    public boolean delete(int id) {
        String query = "DELETE FROM Transaction WHERE transactionid = ?";
        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1,id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
