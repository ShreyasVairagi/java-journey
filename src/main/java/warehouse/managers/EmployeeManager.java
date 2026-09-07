package warehouse.managers;

import warehouse.dao.EmployeeDAO;
import warehouse.model.Employee;
import warehouse.model.Role;
import warehouse.Utility;

public class EmployeeManager {
    Utility util = new Utility();
    EmployeeDAO employeeDAO = new EmployeeDAO();

    public void addEmployee() {
        String name = util.emptyStringValidator("Enter Employee Name: ");
        String email = util.emptyStringValidator("Enter Employee Email: ");
        String phone = util.phoneValidator("Enter Employee Phone: ");
        String address = util.emptyStringValidator("Enter Employee Address: ");
        Role role = util.enumValidator(Role.class, "Enter Employee Role");

        Employee employee = new Employee(name, email, phone, address, role);
        boolean success = employeeDAO.add(employee);

        if (success) {
            System.out.println("Employee added successfully!");
        } else {
            System.out.println("Failed to add employee.");
        }
    }

    public void findEmployee() {
        // Safe integer validation instead of crashing on letters
        int id = util.integerValidator("Enter Employee ID to find");

        Employee employee = employeeDAO.findSingleEmployee(id);
        if (employee != null) {
            System.out.println("\n--- Employee Details ---");
            System.out.println("ID: " + employee.getId());
            System.out.println("Name: " + employee.getName());
            System.out.println("Email: " + employee.getEmail());
            System.out.println("Phone: " + employee.getPhone());
            System.out.println("Address: " + employee.getAddress());
            System.out.println("Role: " + employee.getRole());
        } else {
            System.out.println("Employee with ID " + id + " not found.");
        }
    }

    public void updateEmployee() {
        int id = util.integerValidator("Enter Employee ID to update");

        Employee existing = employeeDAO.findSingleEmployee(id);
        if (existing == null) {
            System.out.println("Employee not found!");
            return;
        }

        String name = util.emptyStringValidator("Enter new Name (current: " + existing.getName() + "): ");
        String email = util.emptyStringValidator("Enter new Email (current: " + existing.getEmail() + "): ");
        String phone = util.phoneValidator("Enter new Phone (current: " + existing.getPhone() + "): ");
        String address = util.emptyStringValidator("Enter new Address (current: " + existing.getAddress() + "): ");

        Role role = util.enumValidator(Role.class, "Enter new Role");

        Employee updatedEmployee = new Employee(id, name, email, phone, address, role);
        boolean success = employeeDAO.update(updatedEmployee);

        if (success) {
            System.out.println("Employee updated successfully!");
        } else {
            System.out.println("Failed to update employee.");
        }
    }

    public void removeEmployee() {
        int id = util.integerValidator("Enter Employee ID to remove");

        boolean success = employeeDAO.delete(id);
        if (success) {
            System.out.println("Employee removed successfully!");
        } else {
            System.out.println("Failed to remove employee.");
        }
    }
}