package warehouse;

import warehouse.dao.InventoryStockDAO;
import warehouse.dao.ProductDAO;
import warehouse.dao.TransactionDAO;
import warehouse.model.Employee;
import warehouse.model.Product;
import warehouse.model.Transaction;
import warehouse.model.TransactionType;

import java.time.LocalDate;
import java.time.LocalTime;

public class InventoryManager {

    //positive number to add or negative number to reduce stock
    public void adjustStock(Product product, Employee employee, int quantity){

        //record transaction
        Transaction transaction = new Transaction(product, employee, quantity, TransactionType.IN, LocalDate.now(), LocalTime.now());
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.add(transaction);

        //update product
        InventoryStockDAO productDAO = new InventoryStockDAO();
        // varibale to store this products quantity
        // array list for db quantites and short them
        // check if user quantity + the last shorted quantity in arraylist is greater than location's capacity
            //fill the current location
            //ask employee to choose another location
            //add remaining items in that location
        //InventoryStockDAO.adjustStockQuantity(product.getId(), quantity);

        // when reducing the quantity from the last index of the array list
        // if the number goes less than 0, then delete that raw from db

    }

//    public moveProduct()
//    public addProduct()
//    public removeProduct()
//    public findProduct()
//    public getLowStockProducts()
//    public calculateInventoryValue()
//    public moveProduct()

}
