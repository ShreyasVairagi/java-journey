package warehouse.managers;

import warehouse.dao.SupplierDAO;
import warehouse.model.Supplier;
import warehouse.Utility;

public class SupplierManager {
    Utility util = new Utility();
    SupplierDAO supplierDAO = new SupplierDAO();

    public void addSupplier() {
        String name = util.emptyStringValidator("Enter Name: ");
        String phone = util.phoneValidator("Enter Phone: ");
        String email = util.emptyStringValidator("Enter Email: ");
        String address = util.emptyStringValidator("Enter Address: ");

        Supplier supplier = new Supplier(name, phone, email, address);
        boolean success = supplierDAO.add(supplier);

        if (success) {
            System.out.println("Supplier added successfully!");
        } else {
            System.out.println("Failed to add supplier.");
        }
    }

    public void findSupplier() {
        int id = util.integerValidator("Enter ID");

        Supplier supplier = supplierDAO.findSingleSupplier(id);
        if (supplier != null) {
            System.out.println("\nSupplier Details");
            System.out.println("ID: " + supplier.getId());
            System.out.println("Name: " + supplier.getName());
            System.out.println("Phone: " + supplier.getPhone());
            System.out.println("Email: " + supplier.getEmail());
            System.out.println("Address: " + supplier.getAddress());
        } else {
            System.out.println("Supplier with ID " + id + " not found.");
        }
    }

    public void updateSupplier() {
        int id = util.integerValidator("Enter ID");

        Supplier existing = supplierDAO.findSingleSupplier(id);
        if (existing == null) {
            System.out.println("Supplier not found!");
            return;
        }

        String name = util.emptyStringValidator("Enter new Name (current: " + existing.getName() + "): ");
        String phone = util.phoneValidator("Enter new Phone (current: " + existing.getPhone() + "): ");
        String email = util.emptyStringValidator("Enter new Email (current: " + existing.getEmail() + "): ");
        String address = util.emptyStringValidator("Enter new Address (current: " + existing.getAddress() + "): ");

        Supplier updatedSupplier = new Supplier(id, name, phone, email, address);
        boolean success = supplierDAO.update(updatedSupplier);

        if (success) {
            System.out.println("Supplier updated successfully!");
        } else {
            System.out.println("Failed to update supplier.");
        }
    }

    public void removeSupplier() {
        int id = util.integerValidator("Enter Supplier ID to remove");

        boolean success = supplierDAO.delete(id); // Fixed: actually calling delete on the DAO
        if (success) {
            System.out.println("Supplier removed successfully!");
        } else {
            System.out.println("Failed to remove supplier.");
        }
    }
}