package warehouse.managers;

import warehouse.dao.SupplierDAO;
import warehouse.model.Supplier;

public class SupplierManager {
    private final SupplierDAO supplierDAO;

    public SupplierManager(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    public void addSupplier(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null.");
        }
        boolean success = supplierDAO.add(supplier);
        if (!success) {
            throw new RuntimeException("Failed to add supplier to the database.");
        }
    }

    public Supplier findSupplier(int id) {
        Supplier supplier = supplierDAO.findSingleSupplier(id);
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier with ID " + id + " not found.");
        }
        return supplier;
    }

    public void updateSupplier(Supplier updatedSupplier) {
        if (updatedSupplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null.");
        }
        Supplier existing = supplierDAO.findSingleSupplier(updatedSupplier.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Supplier not found!");
        }
        boolean success = supplierDAO.update(updatedSupplier);
        if (!success) {
            throw new RuntimeException("Failed to update supplier.");
        }
    }

    public void removeSupplier(int id) {
        Supplier existing = supplierDAO.findSingleSupplier(id);
        if (existing == null) {
            throw new IllegalArgumentException("Supplier with ID " + id + " not found.");
        }
        boolean success = supplierDAO.delete(id);
        if (!success) {
            throw new RuntimeException("Failed to remove supplier.");
        }
    }
}