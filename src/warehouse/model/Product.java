package warehouse.model;

public class Product  {
    private int id;
    private String name;
    private String description;
    private double buyPrice;
    private double sellPrice;
    private int quantity;
    private int minimumStock;
    private int supplier;
    private String storageLocationID;

    public Product(String name, String description, double buyPrice, double sellPrice, int quantity, int minimumStock, int supplier, String storageLocationID) {
        this.name = name;
        this.description = description;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
        this.supplier = supplier;
        this.storageLocationID = storageLocationID;
    }

    public Product(int id, String name, String description, double buyPrice, double sellPrice, int quantity, int minimumStock, int supplier, String storageLocationID) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
        this.supplier = supplier;
        this.storageLocationID = storageLocationID;
    }

    public int getId() {return id;}

    public String getName() {return name;}

    public String getDescription() {return description;}

    public double getBuyPrice() {return buyPrice;}

    public double getSellPrice() {return sellPrice;}

    public int getQuantity() {return quantity;}

    public int getMinimumStock() {return minimumStock;}

    public int getSupplier() {return supplier;}

    public String getStorageLocationID() {return storageLocationID;}
}
