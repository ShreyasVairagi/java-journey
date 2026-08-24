package warehouse;

import warehouse.dao.*;
import warehouse.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class InventoryManager {
    Scanner sc = new Scanner(System.in);

    public void addProduct(){
        ProductDAO productDAO = new ProductDAO();
        System.out.println("Enter name: ");
        String scName = sc.nextLine();
        System.out.println("Enter Description: ");
        String scDescription = sc.nextLine();

        System.out.print("Enter Buy price: ");
        double scBuyPrice = Double.parseDouble(sc.nextLine());

        System.out.print("Enter Sell price: ");
        double scSellPrice = Double.parseDouble(sc.nextLine());

        System.out.println("Enter Minimum Stock Quantity: ");
        int scStockQuantity = Integer.parseInt(sc.nextLine());

        System.out.println("Enter Supplier id: ");
        int scSupplierID = Integer.parseInt(sc.nextLine());

        Supplier sp = new Supplier(scSupplierID);

        Product product = new Product(scName, scDescription, scBuyPrice, scSellPrice, scStockQuantity, sp);
        productDAO.addProduct(product);

        System.out.println("Product added successfully!");
    }

    //positive number to add or negative number to reduce stock
    public void adjustStock(Product product, int quantity) {
        if (quantity == 0) {
            System.out.println("Quantity cannot be zero. Stock adjustment cancelled.");
            return;
        }

        System.out.println("Enter your employee id:");
        String stemployeeId = sc.nextLine();
        int employeeId = Integer.parseInt(stemployeeId);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.findSingleEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found! Stock adjustment cancelled.");
            return;
        }

        // 1. Record transaction
        TransactionType type = (quantity > 0) ? TransactionType.IN : TransactionType.OUT;
        int transactionQuantity = Math.abs(quantity);
        Transaction transaction = new Transaction(product, employee, transactionQuantity, type, LocalDate.now(), LocalTime.now());
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.add(transaction);

        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        List<InventoryStock> existingQuantities = inventoryStockDAO.findStockForProduct(product);

        // 2. Handle positive value
        if (quantity > 0) {
            int userQuantity = quantity;
            Iterator<InventoryStock> it = existingQuantities.iterator();

            // Fill up existing bins first
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

            // Handle overflow
            while (userQuantity > 0) {
                System.out.println("Existing locations are full. Enter new storage location ID for the remaining " + userQuantity + " items:");
                String newLocationId = sc.nextLine();

                int amountForThisBin = Math.min(100, userQuantity);
                inventoryStockDAO.addProductToLocation(product.getId(), newLocationId, amountForThisBin);
                userQuantity -= amountForThisBin;
            }
        }

        // 3. Handle negative value
        else {
            int inputConvertion = Math.abs(quantity);

            for (int i = existingQuantities.size() - 1; i >= 0; i--) {
                InventoryStock currentStock = existingQuantities.get(i);
                int dbQuantity = currentStock.getQuantity();
                int productID = currentStock.getProduct().getId();
                String productLocation = currentStock.getLocation().getLocationID();

                if (dbQuantity >= inputConvertion) {
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
    }

    public void moveProduct(Product product, int quantity){

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero. Move cancelled.");
            return;
        }

        System.out.println("Enter your employee id:");
        String stemployeeId = sc.nextLine();
        int employeeId = Integer.parseInt(stemployeeId);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.findSingleEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found! Stock move cancelled.");
            return;
        }

        // Record transaction
        TransactionType type = TransactionType.MOVE;
        Transaction transaction = new Transaction(product, employee, quantity, type, LocalDate.now(), LocalTime.now());
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.add(transaction);

        // Fetch stock locations
        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        List<InventoryStock> stockList = inventoryStockDAO.findStockForProduct(product);

        if (stockList.isEmpty()) {
            System.out.println("This product has no stock locations to move from.");
            return;
        }

        System.out.println("Current locations for " + product.getName() + ":");
        Iterator<InventoryStock> it = stockList.iterator();
        while (it.hasNext()){
            InventoryStock currentStock = it.next();
            System.out.println("Location ID: " + currentStock.getLocation().getLocationID() + " | Quantity: " + currentStock.getQuantity());
        }

        System.out.println("Enter source product location ID: ");
        String productLocation = sc.nextLine();
        System.out.println("Enter destination location ID: ");
        String destinationLocation = sc.nextLine();

        boolean sourceFound = false;
        boolean moveSuccessful = false;

        for (InventoryStock i : stockList){
            if(i.getLocation().getLocationID().equalsIgnoreCase(productLocation)){
                sourceFound = true;

                if(i.getQuantity() >= quantity){
                    // Subtract from source
                    inventoryStockDAO.updateQuantity(product.getId(), i.getLocation().getLocationID(), -quantity);

                    // If bin is empty, delete the row
                    if (i.getQuantity() - quantity == 0) {
                        inventoryStockDAO.removeLocation(product.getId(), i.getLocation().getLocationID());
                    }

                    moveSuccessful = true;
                } else {
                    System.out.println("Error: The source location only contains " + i.getQuantity() + " items, but you tried to move " + quantity + ".");
                }
                break;
            }
        }

        if (!sourceFound) {
            System.out.println("Error: Source location ID '" + productLocation + "' not found for this product.");
            return;
        }

        // 4. Add quantity
        if (moveSuccessful) {
            inventoryStockDAO.addProductToLocation(product.getId(), destinationLocation, quantity);
            System.out.println("Product moved successfully from " + productLocation + " to " + destinationLocation + "!");
        } else {
            System.out.println("Stock move failed due to insufficient quantity.");
        }
    }

    //Remove product
    //add transaction later
    public void removeProduct(Product product){
        InventoryStockDAO inventoryStockDAO = new InventoryStockDAO();
        inventoryStockDAO.removeLocation(product.getId());
        ProductDAO productDAO = new ProductDAO();
        productDAO.deleteProduct(product.getId());
    }

    //Find product
    public void findProduct(Product product){
        ProductDAO productDAO = new ProductDAO();
        System.out.println(productDAO.findSingleProduct(product.getId()));
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

        for (Transaction t : history) {
            System.out.println(t);
        }
    }

}
