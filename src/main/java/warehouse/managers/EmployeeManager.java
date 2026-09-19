package warehouse.managers;

import warehouse.dao.EmployeeDAO;
import warehouse.dao.SupplierDAO;
import warehouse.model.Employee;
import warehouse.model.Role;
import warehouse.Utility;

public class EmployeeManager {
    private final EmployeeDAO employeeDAO;

    public EmployeeManager(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        boolean success = employeeDAO.add(employee);
        if (!success) {
            throw new RuntimeException("Failed to add employee to the database.");
        }
    }

    public Employee findEmployee(int id) {
        Employee employee = employeeDAO.findSingleEmployee(id);
        if (employee == null) {
            throw new IllegalArgumentException("Employee with ID " + id + " not found.");
        }
        return employee;
    }

    public void updateEmployee(Employee updatedEmployee) {
        if (updatedEmployee == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }
        Employee existing = employeeDAO.findSingleEmployee(updatedEmployee.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Employee not found!");
        }
        boolean success = employeeDAO.update(updatedEmployee);
        if (!success) {
            throw new RuntimeException("Failed to update employee.");
        }
    }

    public void removeEmployee(int id) {
        Employee existing = employeeDAO.findSingleEmployee(id);
        if (existing == null) {
            throw new IllegalArgumentException("Employee with ID " + id + " not found.");
        }
        boolean success = employeeDAO.delete(id);
        if (!success) {
            throw new RuntimeException("Failed to remove employee.");
        }
    }
}