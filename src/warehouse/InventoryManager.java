package warehouse;

import warehouse.dao.EmployeeDAO;
import warehouse.dao.InventoryStockDAO;
import warehouse.dao.ProductDAO;
import warehouse.dao.TransactionDAO;
import warehouse.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public void adjustStock(Product product, StorageLocation storageLocation, int quantity){
        System.out.println("Enter your employee id:");
        String stemployeeId = sc.nextLine();
        int employeeId = Integer.parseInt(stemployeeId);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.findSingleEmployee(employeeId);

        if (employee == null) {
            System.out.println("Employee not found! Stock adjustment cancelled.");
            return;
        }

        //record transaction
        Transaction transaction = new Transaction(product, employee, quantity, TransactionType.IN, LocalDate.now(), LocalTime.now());
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.add(transaction);

        //update product
        InventoryStockDAO productDAO = new InventoryStockDAO();
        // varibale to store this products quantity
        int newStockQuantity = quantity;
        // array list for db quantites and short them

        // check if user quantity + the last shorted quantity in arraylist is greater than location's capacity
            //fill the current location
            //ask employee to choose another location
            //add remaining items in that location
        //InventoryStockDAO.adjustStockQuantity(product.getId(), quantity);

        // when reducing the quantity from the last index of the array list
        // if the number goes less than 0, then delete that raw from db


        InventoryStock inventoryStock = new InventoryStock(product, storageLocation, quantity);

    }

//    public moveProduct()

//    public removeProduct()
//    public findProduct()
//    public getLowStockProducts()
//    public calculateInventoryValue()
//    public moveProduct()

}
