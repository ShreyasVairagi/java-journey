package warehouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import warehouse.DatabaseManager;
import warehouse.model.StorageLocation;

public class StorageLocationDAO {
    //CRUD

    //add
//    public void addLocations() {
//        String sql = "INSERT INTO storagelocation (locationid, aisle, shelf, bin, capacity) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DatabaseManager.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            for (int aisle = 1; aisle <= 6; aisle++) {
//                for (char shelf = 'A'; shelf <= 'G'; shelf++) {
//                    for (int bin = 1; bin <= 10; bin++) {
//                        String locationID = aisle + "-" + shelf + "-" + bin;
//                        int capacity = 100;
//                        pstmt.setString(1, locationID);
//                        pstmt.setInt(2, aisle);
//                        pstmt.setString(3, String.valueOf(shelf));
//                        pstmt.setInt(4, bin);
//                        pstmt.setInt(5, capacity);
//                        pstmt.addBatch();
//                    }
//                }
//            }
//            pstmt.executeBatch();
//            System.out.println("Successfully generated and saved all warehouse locations");
//        } catch (SQLException e) {
//            System.out.println("Error while generating locations:");
//            e.printStackTrace();
//        }
//    }

    //Read all
    public List<String> viewAllLocationIDs() {
        List<String> storageLocations = new ArrayList<>();
        String query = "SELECT locationid FROM storagelocation";
        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                storageLocations.add(rs.getString("locationid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storageLocations;
    }
}
