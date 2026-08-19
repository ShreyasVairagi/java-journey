package warehouse.dao;

import warehouse.DatabaseManager;
import warehouse.model.Employee;
import warehouse.model.Product;
import warehouse.model.Transaction;
import warehouse.model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // ADD
    public boolean add(Transaction transaction) {
        String query = "INSERT INTO Transaction (productid, employeeid, type, quantity, date, time) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // Extract IDs from the objects
            pstmt.setInt(1, transaction.getProduct().getId());
            pstmt.setInt(2, transaction.getEmployee().getId());
            pstmt.setString(3, transaction.getType().name());
            pstmt.setInt(4, transaction.getQuantity());
            pstmt.setObject(5, transaction.getDate());
            pstmt.setObject(6, transaction.getTime());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<Transaction> viewAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transaction";
        try (Connection con = DatabaseManager.connect();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(rs.getInt("productid"));
                Employee employee = new Employee(rs.getInt("employeeid"));

                Transaction t = new Transaction(
                        rs.getInt("transactionid"),
                        product,
                        employee,
                        rs.getInt("quantity"),
                        TransactionType.valueOf(rs.getString("type").toUpperCase()),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("time").toLocalTime()
                );

                transactions.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // UPDATE
    public boolean update(Transaction transaction) {
        String query = "UPDATE Transaction SET productid = ?, employeeid = ?, quantity = ?, type = ?, date = ?, time = ? WHERE transactionid = ?;";
        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, transaction.getProduct().getId());
            pstmt.setInt(2, transaction.getEmployee().getId());
            pstmt.setInt(3, transaction.getQuantity());
            pstmt.setString(4, transaction.getType().name());
            pstmt.setObject(5, transaction.getDate());
            pstmt.setObject(6, transaction.getTime());
            pstmt.setInt(7, transaction.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean delete(int id) {
        String query = "DELETE FROM Transaction WHERE transactionid = ?";
        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}