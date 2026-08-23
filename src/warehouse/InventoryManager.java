package warehouse;

import warehouse.dao.*;
import warehouse.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class InventoryManager {
    Scanner sc = new Scanner(System.in);

//    public void addProduct(String name, String description, double buyPrice, double sellPrice, int StockQuantity, Supplier supplier){
//        Product product = new Product(name, description, buyPrice, sellPrice, StockQuantity, supplier);
//        ProductDAO productDAO = new ProductDAO();
//        System.out.println("Enter name: ");
//        String scName = sc.nextLine();
//        System.out.println("Enter Description: ");
//        String scDescription = sc.nextLine();
//        System.out.print("Enter Buy price: ");
//        String priceInput = sc.nextLine();
//        double scBuyPrice = Double.parseDouble(priceInput);
//        System.out.print("Enter Buy price: ");
//        String sellInput = sc.nextLine();
//        double scSellPrice = Double.parseDouble(sellInput);
//        System.out.println("Enter Stock Quantity: ");
//        String scStockQuantity = sc.nextLine();
//    }

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

//    public moveProduct()

//    public removeProduct()
//    public findProduct()
//    public getLowStockProducts()
//    public calculateInventoryValue()
//    public moveProduct()

}
