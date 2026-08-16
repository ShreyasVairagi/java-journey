package warehouse.dao;

import warehouse.DatabaseManager;
import warehouse.model.Employee;
import warehouse.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    //CRUD

    //add
    public boolean add(Employee employee){
        String query = "INSERT INTO employee (name, email, phone, address , role) VALUES (?, ?, ?, ?, ?);";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement stmt = con.prepareStatement(query)){

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getAddress());
            stmt.setString(5, employee.getRole().name());

            return stmt.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    //Read all
    public List<Employee> viewAll(Employee employee){
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee";
        try(Connection con = DatabaseManager.connect();
        PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()){
                Employee e = new Employee(rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        Role.valueOf(rs.getString("role").toUpperCase())
                );
                employees.add(e);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return employees;
    }

    //update
    public boolean update(Employee employee){
        String query = "UPDATE Employee SET name = ?, email = ?, phone = ?, address = ?, roles = ? WHERE employeeid = ?";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getAddress());
            stmt.setString(5, employee.getRole().name());
            stmt.setInt(6,employee.getId());

            return stmt.executeUpdate() > 0;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //delete
    public boolean delete(int id) {
        String query = "DELETE FROM Employee WHERE employeeid = ?";
        try (Connection con = DatabaseManager.connect();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
