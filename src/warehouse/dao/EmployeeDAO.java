package warehouse.dao;

import warehouse.managers.DatabaseManager;
import warehouse.model.Employee;
import warehouse.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    //CRUD

    //add
    public boolean add(Employee employee){
        String query = "INSERT INTO employee (name, email, phone, address, role) VALUES (?, ?, ?, ?, ?);";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement pstmt = con.prepareStatement(query)){

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setString(3, employee.getPhone());
            pstmt.setString(4, employee.getAddress());
            pstmt.setString(5, employee.getRole().name());

            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    //Read all
    public List<Employee> viewAll(){
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employee";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()){
                Employee e = new Employee(
                        rs.getInt("employeeid"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        Role.valueOf(rs.getString("role").toUpperCase())
                );
                employees.add(e);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return employees;
    }

    //update
    public boolean update(Employee employee){
        String query = "UPDATE employee SET name = ?, email = ?, phone = ?, address = ?, role = ? WHERE employeeid = ?";
        try(Connection con = DatabaseManager.connect();
            PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setString(3, employee.getPhone());
            pstmt.setString(4, employee.getAddress());
            pstmt.setString(5, employee.getRole().name());
            pstmt.setInt(6, employee.getId());

            return pstmt.executeUpdate() > 0;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    //delete
    public boolean delete(int id) {
        String query = "DELETE FROM employee WHERE employeeid = ?";
        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Employee findSingleEmployee(int id) {
        String query = "SELECT * FROM employee WHERE employeeid = ?";
        Employee employee = null;
        try (Connection con = DatabaseManager.connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int empId = rs.getInt("employeeid");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    Role role = Role.valueOf(rs.getString("role").toUpperCase());
                    employee = new Employee(empId, name, email, phone, address, role);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return employee;
    }
}