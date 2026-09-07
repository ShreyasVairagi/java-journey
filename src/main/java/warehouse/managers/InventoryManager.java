package warehouse.managers;

import warehouse.dao.*;
import warehouse.model.*;
import warehouse.Utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;

public class InventoryManager {
    Utility util = new Utility();

    public void addProduct(){
        ProductDAO productDAO = new ProductDAO();
        SupplierDAO supplierDAO = new SupplierDAO();

        String userInputName = util.emptyStringValidator("Enter name: ");
        String userInputDescription = util.emptyStringValidator("Enter Description: ");

        double userInputBuyPrice = 0.0;
        while (true) {
            userInputBuyPrice = util.doubleValidator("Enter Buy price: ");
            if (userInputBuyPrice > 0) {
                break;
            }
            System.out.println("Buy price must be a positive number greater than 0.");
        }

        double userInputSellPrice = 0.0;
        while (true) {
            userInputSellPrice = util.doubleValidator("Enter Sell price: ");
            if (userInputSellPrice > 0) {
                break;
            }
            System.out.println("Sell price must be a positive number greater than 0.");
        }

        int userInputStockQuantity = 0;
        while (true) {
            userInputStockQuantity = util.integerValidator("Enter Minimum Stock Quantity: ");
            if (userInputStockQuantity > 0) {
                break;
            }
            System.out.println("Minimum stock quantity must be greater than 0.");
        }

        int userInputSupplierID = 0;
        while (true) {
            userInputSupplierID = util.integerValidator("Enter Supplier id: ");
            Supplier existingSupplier = supplierDAO.findSingleSupplier(userInputSupplierID);
            if (existingSupplier != null) {
                break;
            }
            System.out.println("Error: Supplier ID " + userInputSupplierID + " does not exist. Please enter a valid supplier ID.");
        }

        Supplier sp = new Supplier(userInputSupplierID);

        Product product = new Product(userInputName, userInputDescription, userInputBuyPrice, userInputSellPrice, userInputStockQuantity, sp);
        productDAO.addProduct(product);

        System.out.println("Product added successfully!");
    }

    //positive number to add or negative number to reduce stock
    public void adjustStock(Product product, int quantity) {
        if (quantity == 0) {
            System.out.println("Quantity cannot be zero. Stock adjustment cancelled.");
            return;
        }

        int employeeId = util.integerValidator("Enter your employee id");

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.findSingleEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found! Stock adjustment cancelled.");
            return;
        }

        // --- NEW: Check if the product actually exists before doing anything ---
        ProductDAO productDAO = new ProductDAO();
        Product existingProduct = productDAO.findSingleProduct(product.getId());
        if (existingProduct == null) {
            System.out.println("Error: Product with ID " + product.getId() + " does not exist. Stock adjustment cancelled.");
            return;
        }

        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        List<InventoryStock> existingQuantities = inventoryStockDAO.findStockForProduct(product);

        // Validate removal request early if quantity is negative
        if (quantity < 0) {
            int totalAvailableStock = 0;
            for (InventoryStock stock : existingQuantities) {
                totalAvailableStock += stock.getQuantity();
            }

            int requestedRemoval = Math.abs(quantity);
            if (totalAvailableStock < requestedRemoval) {
                System.out.println("Error: Cannot remove " + requestedRemoval + " items. Total available stock across all locations is only " + totalAvailableStock + ". Adjustment cancelled.");
                return;
            }
        }

        // Handle positive value (Adding stock)
        if (quantity > 0) {
            int userQuantity = quantity;

            // If the product has no locations yet
            if (existingQuantities.isEmpty()) {
                while (userQuantity > 0) {
                    String newLocationId;
                    while (true) {
                        newLocationId = util.emptyStringValidator("Product has no assigned stock. Enter storage location ID for the " + userQuantity + " items: ");
                        if (inventoryStockDAO.locationExists(newLocationId)) {
                            break;
                        }
                        System.out.println("Error: Location ID does not exist in the warehouse. Please enter a valid location.");
                    }

                    int amountForThisBin = Math.min(100, userQuantity);
                    inventoryStockDAO.addProductToLocation(product.getId(), newLocationId, amountForThisBin);
                    userQuantity -= amountForThisBin;
                }
            } else {
                // Fill up existing bins
                Iterator<InventoryStock> it = existingQuantities.iterator();

                while (it.hasNext() && userQuantity > 0) {
                    InventoryStock currentStock = it.next();
                    int dbQuantity = currentStock.getQuantity();
                    int productID = currentStock.getProduct().getId();
                    String productLocation = currentStock.getLocation().getLocationID();

                    int spaceAvailable = 100 - dbQuantity;

                    if (spaceAvailable > 0) {
                        int amountToAdd = Math.min(spaceAvailable, userQuantity);
                        inventoryStockDAO.updateQuantity(productID, productLocation, amountToAdd);
                        userQuantity -= amountToAdd;
                    }
                }

                // Handle overflow with location validation
                while (userQuantity > 0) {
                    String newLocationId;
                    while (true) {
                        newLocationId = util.emptyStringValidator("Existing locations are full. Enter new storage location ID for the remaining " + userQuantity + " items: ");
                        if (inventoryStockDAO.locationExists(newLocationId)) {
                            break;
                        }
                        System.out.println("Error: Location ID does not exist in the warehouse. Please enter a valid location.");
                    }

                    int amountForThisBin = Math.min(100, userQuantity);
                    inventoryStockDAO.addProductToLocation(product.getId(), newLocationId, amountForThisBin);
                    userQuantity -= amountForThisBin;
                }
            }
        }

        // Handle negative value (Reducing stock)
        else {
            int inputConvertion = Math.abs(quantity);

            for (int i = existingQuantities.size() - 1; i >= 0; i--) {
                InventoryStock currentStock = existingQuantities.get(i);
                int dbQuantity = currentStock.getQuantity();
                int productID = currentStock.getProduct().getId();
                String productLocation = currentStock.getLocation().getLocationID();

                if (dbQuantity > inputConvertion) {
                    inventoryStockDAO.updateQuantity(productID, productLocation, -inputConvertion);
                    inputConvertion = 0;
                } else {
                    inventoryStockDAO.removeLocation(productID, productLocation);
                    inputConvertion -= dbQuantity;
                }

                if (inputConvertion == 0) {
                    break;
                }
            }
        }

        // Record transaction
        TransactionType type = (quantity > 0) ? TransactionType.IN : TransactionType.OUT;
        int transactionQuantity = Math.abs(quantity);
        Transaction transaction = new Transaction(product, employee, transactionQuantity, type, LocalDate.now(), LocalTime.now());
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.add(transaction);

        System.out.println("Stock adjusted successfully!");
    }

    public void moveProduct(Product product, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero. Move cancelled.");
            return;
        }

        ProductDAO productDAO = new ProductDAO();

        // 1. Fetch full product details so it doesn't show as 'null'
        Product existingProduct = productDAO.findSingleProduct(product.getId());
        if (existingProduct == null) {
            System.out.println("Error: Product with ID " + product.getId() + " does not exist. Move cancelled.");
            return;
        }

        int employeeId = util.integerValidator("Enter your employee id");
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.findSingleEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found! Move cancelled.");
            return;
        }

        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        List<InventoryStock> stockList = inventoryStockDAO.findStockForProduct(existingProduct);

        if (stockList.isEmpty()) {
            System.out.println("This product has no stock assigned to any location.");
            return;
        }

        System.out.println("\nCurrent locations for " + existingProduct.getName() + ":");
        for (InventoryStock stock : stockList) {
            System.out.println("Location ID: " + stock.getLocation().getLocationID() + " | Quantity: " + stock.getQuantity());
        }

        String sourceLocationId = util.emptyStringValidator("Enter source product location ID: ");
        String destinationLocationId = util.emptyStringValidator("Enter destination location ID: ");

        // 2. Validate destination location exists
        if (!inventoryStockDAO.locationExists(destinationLocationId)) {
            System.out.println("Error: Destination location ID '" + destinationLocationId + "' does not exist. Move cancelled.");
            return;
        }

        // 3. Find source stock
        InventoryStock sourceStock = null;
        for (InventoryStock stock : stockList) {
            if (stock.getLocation().getLocationID().equalsIgnoreCase(sourceLocationId)) {
                sourceStock = stock;
                break;
            }
        }

        if (sourceStock == null) {
            System.out.println("Error: Source location ID '" + sourceLocationId + "' not found for this product. Move cancelled.");
            return;
        }

        // 4. Validate sufficient stock
        if (sourceStock.getQuantity() < quantity) {
            System.out.println("Error: Source location only contains " + sourceStock.getQuantity() + " items, but you tried to move " + quantity + ". Move cancelled.");
            return;
        }

        // 5. Execute Database Changes safely
        boolean added = inventoryStockDAO.addProductToLocation(existingProduct.getId(), destinationLocationId, quantity);
        if (!added) {
            System.out.println("Failed to add product to destination location. Move cancelled.");
            return;
        }

        // Subtract from source
        inventoryStockDAO.updateQuantity(existingProduct.getId(), sourceLocationId, -quantity);

        // If source is now empty, remove location row
        if (sourceStock.getQuantity() - quantity == 0) {
            inventoryStockDAO.removeLocation(existingProduct.getId(), sourceLocationId);
        }

        // 6. Record transaction only after everything succeeds
        TransactionType type = TransactionType.MOVE;
        Transaction transaction = new Transaction(existingProduct, employee, quantity, type, LocalDate.now(), LocalTime.now());
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.add(transaction);

        System.out.println("Product moved successfully from " + sourceLocationId + " to " + destinationLocationId + "!");
    }

    public void removeProduct(Product product){
        ProductDAO productDAO = new ProductDAO();

        Product existingProduct = productDAO.findSingleProduct(product.getId());
        if (existingProduct == null) {
            System.out.println("Product with ID " + product.getId() + " not found. Removal cancelled.");
            return;
        }

        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        inventoryStockDAO.removeLocation(product.getId());

        boolean success = productDAO.deleteProduct(product.getId());
        if (success) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Failed to remove product.");
        }
    }

    public void findProduct(Product product){
        ProductDAO productDAO = new ProductDAO();
        Product foundProduct = productDAO.findSingleProduct(product.getId());
        if(foundProduct == null){
            System.out.println("No product found");
            return;
        }
        System.out.println("Product id :" + foundProduct.getId() +
                "Product Name : " +foundProduct.getName() +
                "Description"+ foundProduct.getDescription()+
                "Supplier" + foundProduct.getSupplier());
    }

    public void getLowStockProducts(){
        ProductDAO productDAO = new ProductDAO();
        List<Product> listOfProducts = productDAO.viewAllProducts();

        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();

        System.out.println("Low Stock Report");

        for (Product product : listOfProducts) {
            List<InventoryStock> stockList = inventoryStockDAO.findStockForProduct(product);

            int totalQuantity = 0;
            for (InventoryStock stock : stockList) {
                totalQuantity += stock.getQuantity();
            }

            if (totalQuantity <= product.getMinimumStock()) {
                System.out.println("Product: " + product.getName()
                        + " | Current Stock: " + totalQuantity
                        + " | Minimum Required: " + product.getMinimumStock());
            }
        }
    }

    public void calculateInventoryValue() {
        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        double totalValue = inventoryStockDAO.getTotalInventoryValue();

        System.out.println("Total Inventory Value: £" + String.format("%.2f", totalValue));
    }

    public void getProductTransactionHistory(Product product){
        TransactionDAO transactionDAO = new TransactionDAO();
        List<Transaction> history = transactionDAO.getProductTransactionHistory(product.getId());

        if (history.isEmpty()) {
            System.out.println("No transaction history found for this product.");
            return;
        }

        for (Transaction transaction : history) {
            System.out.println(transaction.getId() +" "+
                    transaction.getDate() +" "+
                    transaction.getTime() +" "+
                    transaction.getProduct() + " " +
                    transaction.getEmployee() +" "+
                    transaction.getEmployee().getName());
        }
    }

    public void viewStockForProduct(Product product) {
        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        List<InventoryStock> stockList = inventoryStockDAO.findStockForProduct(product);

        if (stockList.isEmpty()) {
            System.out.println("No stock locations found for Product ID " + product.getId());
            return;
        }
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
}