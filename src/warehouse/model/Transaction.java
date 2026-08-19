package warehouse.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private int id;
    private Product product;       // Full Product object
    private Employee employee;     // Full Employee object
    private int quantity;
    private TransactionType type;
    private LocalDate date;        // Proper date type
    private LocalTime time;        // Proper time type

    // Constructor without ID
    public Transaction(Product product, Employee employee, int quantity, TransactionType type, LocalDate date, LocalTime time) {
        this.product = product;
        this.employee = employee;
        this.quantity = quantity;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    // Constructor with ID
    public Transaction(int id, Product product, Employee employee, int quantity, TransactionType type, LocalDate date, LocalTime time) {
        this.id = id;
        this.product = product;
        this.employee = employee;
        this.quantity = quantity;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    public int getId() { return id; }

    public Product getProduct() { return product; }

    public Employee getEmployee() { return employee; }

    public int getQuantity() { return quantity; }

    public TransactionType getType() { return type; }

    public LocalDate getDate() { return date; }

    public LocalTime getTime() { return time; }
}