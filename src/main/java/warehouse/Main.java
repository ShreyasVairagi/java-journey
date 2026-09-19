package warehouse;

import warehouse.dao.*;
import warehouse.managers.EmployeeManager;
import warehouse.managers.InventoryManager;
import warehouse.managers.SupplierManager;
import warehouse.model.*;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        ProductDAO productDAO = new ProductDAO();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        InventoryManager inventoryManager = new InventoryManager(inventoryStockDAO, productDAO, employeeDAO, transactionDAO, supplierDAO);
        SupplierManager supplierManager = new SupplierManager(supplierDAO);
        EmployeeManager employeeManager = new EmployeeManager(employeeDAO);
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
                                String userInputName = util.emptyStringValidator("Enter name: ");
                                String userInputDescription = util.emptyStringValidator("Enter Description: ");

                                double userInputBuyPrice;
                                while (true) {
                                    userInputBuyPrice = util.doubleValidator("Enter Buy price: ");
                                    if (userInputBuyPrice > 0) {
                                        break;
                                    }
                                    System.out.println("Buy price must be a positive number greater than 0.");
                                }

                                double userInputSellPrice;
                                while (true) {
                                    userInputSellPrice = util.doubleValidator("Enter Sell price: ");
                                    if (userInputSellPrice > 0) {
                                        break;
                                    }
                                    System.out.println("Sell price must be a positive number greater than 0.");
                                }

                                int userInputStockQuantity;
                                while (true) {
                                    userInputStockQuantity = util.integerValidator("Enter Minimum Stock Quantity: ");
                                    if (userInputStockQuantity > 0) {
                                        break;
                                    }
                                    System.out.println("Minimum stock quantity must be greater than 0.");
                                }

                                int userInputSupplierID;
                                while (true) {
                                    userInputSupplierID = util.integerValidator("Enter Supplier id: ");
                                    if (inventoryManager.supplierExists(userInputSupplierID)) {
                                        break;
                                    }
                                    System.out.println("Error: Supplier ID " + userInputSupplierID + " does not exist. Please enter a valid supplier ID.");
                                }
                                Supplier sp = new Supplier(userInputSupplierID);
                                Product product = new Product(userInputName, userInputDescription, userInputBuyPrice, userInputSellPrice, userInputStockQuantity, sp);
                                inventoryManager.addProduct(product);
                                break;

                            case 2:
                                int findId = util.integerValidator("Enter Product ID");

                                try {
                                    Product foundProduct = inventoryManager.findProduct(findId);
                                    System.out.println("\n--- Product Details ---");
                                    System.out.println("Product ID   : " + foundProduct.getId());
                                    System.out.println("Product Name : " + foundProduct.getName());
                                    System.out.println("Description  : " + foundProduct.getDescription());
                                    System.out.println("Supplier     : " + foundProduct.getSupplier());
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 3:
                                int removeId = util.integerValidator("Enter Product ID to remove");
                                try {
                                    Product existingProduct = inventoryManager.findProduct(removeId); // Uses manager!
                                    inventoryManager.removeProduct(existingProduct);
                                    System.out.println("Product removed successfully!");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
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

                                try {
                                    List<InventoryStock> stockList = inventoryManager.viewStockForProduct(viewPId);

                                    if (stockList.isEmpty()) {
                                        System.out.println("No stock locations found for Product ID " + viewPId);
                                    } else {
                                        System.out.println("\n--- Stock Locations Report ---");
                                        int totalStock = 0;
                                        for (InventoryStock stock : stockList) {
                                            System.out.println("Location ID: " + stock.getLocation().getLocationID()
                                                    + " | Aisle: " + stock.getLocation().getAisle()
                                                    + " | Shelf: " + stock.getLocation().getShelf()
                                                    + " | Bin: " + stock.getLocation().getBin()
                                                    + " | Quantity: " + stock.getQuantity());
                                            totalStock += stock.getQuantity();
                                        }
                                        System.out.println("Total Combined Stock: " + totalStock);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                                break;
                            case 2:
                                int employeeId = util.integerValidator("Enter your employee id");
                                int pId = util.integerValidator("Enter Product ID");
                                int qty = util.integerValidator("Enter quantity adjustment (+ to add, - to reduce)");

                                try {
                                    Product product = inventoryManager.findProduct(pId);
                                    List<InventoryStock> existingQuantities = inventoryManager.viewStockForProduct(pId);

                                    String locationId = null;

                                    if (existingQuantities.isEmpty() && qty > 0) {
                                        System.out.println("Product has no assigned stock locations.");
                                        while (true) {
                                            locationId = util.emptyStringValidator("Enter storage location ID for these items: ");
                                            if (inventoryManager.locationExists(locationId)) {
                                                break;
                                            }
                                            System.out.println("Error: Location ID does not exist. Please try again.");
                                        }
                                    }

                                    while (true) {
                                        try {
                                            inventoryManager.adjustStock(product, qty, employeeId, locationId);
                                            System.out.println("Stock adjusted successfully!");
                                            break;
                                        } catch (IllegalStateException e) {
                                            if (e.getMessage().startsWith("OVERFLOW_LOCATION_REQUIRED")) {
                                                System.out.println("Existing locations are full! Additional storage location needed.");
                                                while (true) {
                                                    locationId = util.emptyStringValidator("Enter new storage location ID for remaining items: ");
                                                    if (inventoryManager.locationExists(locationId)) {
                                                        break;
                                                    }
                                                    System.out.println("Error: Location ID does not exist. Please try again.");
                                                }
                                            } else {
                                                System.out.println("Error: " + e.getMessage());
                                                break;
                                            }
                                        } catch (IllegalArgumentException e) {
                                            System.out.println(e.getMessage());
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 3:
                                int movePId = util.integerValidator("Enter Product ID");
                                try {
                                    Product existingProduct = inventoryManager.findProduct(movePId);

                                    int moveQty = util.integerValidator("Enter quantity to move");
                                    while (moveQty <= 0){
                                        moveQty = util.integerValidator("Enter quantity to move");
                                    }

                                    int employeeID = util.integerValidator("Enter your employee id");
                                    Employee employee = employeeManager.findEmployee(employeeID);
                                    if (employee == null) {
                                        System.out.println("Error: Employee not found.");
                                        break;
                                    }

                                    List<InventoryStock> stockList = inventoryManager.viewStockForProduct(movePId);

                                    if (stockList.isEmpty()) {
                                        System.out.println("This product has no stock assigned to any location.");
                                        break;
                                    }

                                    System.out.println("\nCurrent locations for " + existingProduct.getName() + ":");
                                    for (InventoryStock stock : stockList) {
                                        System.out.println("Location ID: " + stock.getLocation().getLocationID() + " | Quantity: " + stock.getQuantity());
                                    }

                                    String sourceLoc = util.emptyStringValidator("Enter source product location ID: ");
                                    String destLoc = util.emptyStringValidator("Enter destination location ID: ");

                                    inventoryManager.moveProduct(existingProduct, moveQty, employee, sourceLoc, destLoc);
                                    System.out.println("Product moved successfully from " + sourceLoc + " to " + destLoc + "!");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 4:
                                stockMenu = false;
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;

                // 3. Supplier Management
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
                                String name = util.emptyStringValidator("Enter Name: ");
                                String phone = util.phoneValidator("Enter Phone: ");
                                String email = util.emptyStringValidator("Enter Email: ");
                                String address = util.emptyStringValidator("Enter Address: ");

                                Supplier newSupplier = new Supplier(name, phone, email, address);
                                try {
                                    supplierManager.addSupplier(newSupplier);
                                    System.out.println("Supplier added successfully!");
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                                break;

                            case 2:
                                int findId = util.integerValidator("Enter ID");
                                try {
                                    Supplier supplier = supplierManager.findSupplier(findId);
                                    System.out.println("\n--- Supplier Details ---");
                                    System.out.println("ID: " + supplier.getId());
                                    System.out.println("Name: " + supplier.getName());
                                    System.out.println("Phone: " + supplier.getPhone());
                                    System.out.println("Email: " + supplier.getEmail());
                                    System.out.println("Address: " + supplier.getAddress());
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case 3:
                                int updateId = util.integerValidator("Enter ID");
                                try {
                                    Supplier existing = supplierManager.findSupplier(updateId);

                                    String newName = util.emptyStringValidator("Enter new Name (current: " + existing.getName() + "): ");
                                    String newPhone = util.phoneValidator("Enter new Phone (current: " + existing.getPhone() + "): ");
                                    String newEmail = util.emptyStringValidator("Enter new Email (current: " + existing.getEmail() + "): ");
                                    String newAddress = util.emptyStringValidator("Enter new Address (current: " + existing.getAddress() + "): ");

                                    Supplier updatedSupplier = new Supplier(updateId, newName, newPhone, newEmail, newAddress);
                                    supplierManager.updateSupplier(updatedSupplier);
                                    System.out.println("Supplier updated successfully!");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case 4:
                                int removeId = util.integerValidator("Enter Supplier ID to remove");
                                try {
                                    supplierManager.removeSupplier(removeId);
                                    System.out.println("Supplier removed successfully!");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case 5:
                                supplierMenu = false;
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;

                // 4. Employee Management
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
                                String name = util.emptyStringValidator("Enter Employee Name: ");
                                String email = util.emptyStringValidator("Enter Employee Email: ");
                                String phone = util.phoneValidator("Enter Employee Phone: ");
                                String address = util.emptyStringValidator("Enter Employee Address: ");
                                Role role = util.enumValidator(Role.class, "Enter Employee Role");

                                Employee newEmployee = new Employee(name, email, phone, address, role);
                                try {
                                    employeeManager.addEmployee(newEmployee);
                                    System.out.println("Employee added successfully!");
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                                break;

                            case 2:
                                int findId = util.integerValidator("Enter Employee ID to find");
                                try {
                                    Employee employee = employeeManager.findEmployee(findId);
                                    System.out.println("\n--- Employee Details ---");
                                    System.out.println("ID: " + employee.getId());
                                    System.out.println("Name: " + employee.getName());
                                    System.out.println("Email: " + employee.getEmail());
                                    System.out.println("Phone: " + employee.getPhone());
                                    System.out.println("Address: " + employee.getAddress());
                                    System.out.println("Role: " + employee.getRole());
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case 3:
                                int updateId = util.integerValidator("Enter Employee ID to update");
                                try {
                                    Employee existing = employeeManager.findEmployee(updateId);

                                    String newName = util.emptyStringValidator("Enter new Name (current: " + existing.getName() + "): ");
                                    String newEmail = util.emptyStringValidator("Enter new Email (current: " + existing.getEmail() + "): ");
                                    String newPhone = util.phoneValidator("Enter new Phone (current: " + existing.getPhone() + "): ");
                                    String newAddress = util.emptyStringValidator("Enter new Address (current: " + existing.getAddress() + "): ");
                                    Role newRole = util.enumValidator(Role.class, "Enter new Role");

                                    Employee updatedEmployee = new Employee(updateId, newName, newEmail, newPhone, newAddress, newRole);
                                    employeeManager.updateEmployee(updatedEmployee);
                                    System.out.println("Employee updated successfully!");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case 4:
                                int removeId = util.integerValidator("Enter Employee ID to remove");
                                try {
                                    employeeManager.removeEmployee(removeId);
                                    System.out.println("Employee removed successfully!");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
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
                                try {
                                    List<InventoryManager.LowStockItem> lowStockItems = inventoryManager.getLowStockProducts();

                                    System.out.println("\n--- Low Stock Report ---");
                                    if (lowStockItems.isEmpty()) {
                                        System.out.println("All products are sufficiently stocked!");
                                    } else {
                                        for (InventoryManager.LowStockItem item : lowStockItems) {
                                            System.out.println("Product: " + item.getProduct().getName()
                                                    + " | Current Stock: " + item.getTotalQuantity()
                                                    + " | Minimum Required: " + item.getProduct().getMinimumStock());
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                                break;
                            case 2:
                                try {
                                    double totalValue = inventoryManager.calculateInventoryValue();

                                    System.out.println("\n--- Inventory Value Report ---");
                                    System.out.println("Total Inventory Value: £" + String.format("%.2f", totalValue));
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                                break;
                            case 3: // (Update your case number to match your menu)
                                int histPId = util.integerValidator("Enter Product ID for history");

                                try {
                                    List<Transaction> history = inventoryManager.getProductTransactionHistory(histPId);

                                    System.out.println("\n--- Transaction History Report ---");
                                    if (history.isEmpty()) {
                                        System.out.println("No transaction history found for this product.");
                                    } else {
                                        for (Transaction transaction : history) {
                                            System.out.println("ID: " + transaction.getId() +
                                                    " | Date: " + transaction.getDate() +
                                                    " | Time: " + transaction.getTime() +
                                                    " | Type: " + transaction.getType() +
                                                    " | Qty: " + transaction.getQuantity() +
                                                    " | Employee: " + transaction.getEmployee().getName());
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
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
