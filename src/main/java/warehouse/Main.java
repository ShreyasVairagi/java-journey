package warehouse;

import warehouse.dao.InventoryStockDAO;
import warehouse.managers.EmployeeManager;
import warehouse.managers.InventoryManager;
import warehouse.managers.SupplierManager;
import warehouse.model.InventoryStock;
import warehouse.model.Product;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        SupplierManager supplierManager = new SupplierManager();
        EmployeeManager employeeManager = new EmployeeManager();
        Utility util = new Utility();

        boolean running = true;

        while (running) {
            System.out.println("***** WAREHOUSE MANAGEMENT SYSTEM *****");
            System.out.println("1. Product Management");
            System.out.println("2. Stock Control");
            System.out.println("3. Supplier Management");
            System.out.println("4. Employee Management");
            System.out.println("5. Location Management");
            System.out.println("6. Exit");

            int choice = util.integerValidator("Enter your choice: ");

            switch (choice) {
                //1. Product Management
                case 1:
                    boolean productMenu = true;
                    while (productMenu) {
                        System.out.println("\nProduct Management");
                        System.out.println("1. Add Product");
                        System.out.println("2. Find Product");
                        System.out.println("3. Remove Product");
                        System.out.println("4. Back");

                        int subChoice = util.integerValidator("Enter choice");
                        switch (subChoice) {
                            case 1:
                                manager.addProduct();
                                break;
                            case 2:
                                int findId = util.integerValidator("Enter Product ID");
                                manager.findProduct(new Product(findId));
                                break;
                            case 3:
                                int removeId = util.integerValidator("Enter Product ID to remove");
                                manager.removeProduct(new Product(removeId));
                                break;
                            case 4:
                                productMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice. Please choose between 1 and 4.");
                        }
                    }
                    break;

                //2. Stock Control
                case 2:
                    boolean stockMenu = true;
                    while (stockMenu) {
                        System.out.println("\nStock Control");
                        System.out.println("1. View Stock");
                        System.out.println("2. Adjust Stock");
                        System.out.println("3. Move Product");
                        System.out.println("4. Back");

                        int subChoice = util.integerValidator("Enter choice");
                        switch (subChoice) {
                            case 1:
                                int viewPId = util.integerValidator("Enter Product ID to view stock");
                                manager.viewStockForProduct(new Product(viewPId));
                                break;
                            case 2:
                                int employeeId = util.integerValidator("Enter your employee id");
                                int pId = util.integerValidator("Enter Product ID");
                                int qty = util.integerValidator("Enter quantity adjustment (+ to add, - to reduce)");

                                Product product = new Product(pId);
                                InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
                                List<InventoryStock> existingQuantities = inventoryStockDAO.findStockForProduct(product);

                                String locationId = null;

                                // 1. If it has no locations initially
                                if (existingQuantities.isEmpty() && qty > 0) {
                                    System.out.println("Product has no assigned stock locations.");
                                    while (true) {
                                        locationId = util.emptyStringValidator("Enter storage location ID for these items: ");
                                        if (inventoryStockDAO.locationExists(locationId)) {
                                            break;
                                        }
                                        System.out.println("Error: Location ID does not exist. Please try again.");
                                    }
                                }

                                while (true) {
                                    try {
                                        manager.adjustStock(product, qty, employeeId, locationId);
                                        System.out.println("Stock adjusted successfully!");
                                        break;
                                    } catch (IllegalStateException e) {
                                        // Handle overflow
                                        if (e.getMessage().startsWith("OVERFLOW_LOCATION_REQUIRED")) {
                                            System.out.println("Existing locations are full! Additional storage location needed.");

                                            // Ask the user for the new overflow location
                                            while (true) {
                                                locationId = util.emptyStringValidator("Enter new storage location ID for remaining items: ");
                                                if (inventoryStockDAO.locationExists(locationId)) {
                                                    break;
                                                }
                                                System.out.println("Error: Location ID does not exist. Please try again.");
                                            }
                                        } else {
                                            // Any other IllegalStateException
                                            System.out.println("Error: " + e.getMessage());
                                            break;
                                        }
                                    } catch (IllegalArgumentException e) {
                                        System.out.println(e.getMessage());
                                        break;
                                    }
                                }
                                break;
                            case 3:
                                int movePId = util.integerValidator("Enter Product ID");
                                int moveQty = util.integerValidator("Enter quantity to move");
                                manager.moveProduct(new Product(movePId), moveQty);
                                break;
                            case 4:
                                stockMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;

                //3. Supplier Management
                case 3:
                    boolean supplierMenu = true;
                    while (supplierMenu) {
                        System.out.println("\nSupplier Management");
                        System.out.println("1. Add Supplier");
                        System.out.println("2. Find Supplier");
                        System.out.println("3. Update Supplier");
                        System.out.println("4. Remove Supplier");
                        System.out.println("5. Back");

                        int subChoice = util.integerValidator("Enter choice");
                        switch (subChoice) {
                            case 1:
                                supplierManager.addSupplier();
                                break;
                            case 2:
                                supplierManager.findSupplier();
                                break;
                            case 3:
                                supplierManager.updateSupplier();
                                break;
                            case 4:
                                supplierManager.removeSupplier();
                                break;
                            case 5:
                                supplierMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;

                //4. Employee Management
                case 4:
                    boolean employeeMenu = true;
                    while (employeeMenu) {
                        System.out.println("\n--- Employee Management ---");
                        System.out.println("1. Add Employee");
                        System.out.println("2. Find Employee");
                        System.out.println("3. Update Employee");
                        System.out.println("4. Remove Employee");
                        System.out.println("5. Back");

                        int subChoice = util.integerValidator("Enter choice");
                        switch (subChoice) {
                            case 1:
                                employeeManager.addEmployee();
                                break;
                            case 2:
                                employeeManager.findEmployee();
                                break;
                            case 3:
                                employeeManager.updateEmployee();
                                break;
                            case 4:
                                employeeManager.removeEmployee();
                                break;
                            case 5:
                                employeeMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;

                //5. Reports
                case 5:
                    boolean reportMenu = true;
                    while (reportMenu) {
                        System.out.println("\n--- Reports ---");
                        System.out.println("1. Low Stock Products");
                        System.out.println("2. Calculate Inventory Value");
                        System.out.println("3. Product Transaction History");
                        System.out.println("4. Back");

                        int subChoice = util.integerValidator("Enter choice");
                        switch (subChoice) {
                            case 1:
                                manager.getLowStockProducts();
                                break;
                            case 2:
                                manager.calculateInventoryValue();
                                break;
                            case 3:
                                int histPId = util.integerValidator("Enter Product ID for history");
                                manager.getProductTransactionHistory(new Product(histPId));
                                break;
                            case 4:
                                reportMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;

                //6. Exit
                case 6:
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }
}
