package warehouse.model;

public class Transaction {
    private int id;
    private int product;
    private int employee;
    private int quantity;
    private TransactionType type;
    private int date;
    private int time;

    public Transaction(int id, int product, int employee, int quantity, TransactionType type, int date, int time) {
        this.id = id;
        this.product = product;
        this.employee = employee;
        this.quantity = quantity;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    public int getId() {return id;}

    public int getProduct() {return product;}

    public int getEmployee() {return employee;}

    public int getQuantity() {return quantity;}

    public TransactionType getType() {return type;}

    public int getDate() {return date;}

    public int getTime() {return time;}
}
