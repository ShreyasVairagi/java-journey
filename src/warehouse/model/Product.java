package warehouse.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double buyPrice;
    private double sellPrice;
    private int minimumStock;
    private Supplier supplier;

    public Product(String name, String description, double buyPrice, double sellPrice, int minimumStock, Supplier supplier) {
        this.name = name;
        this.description = description;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.minimumStock = minimumStock;
        this.supplier = supplier;
    }

    public Product(int id, String name, String description, double buyPrice, double sellPrice,  int minimumStock, Supplier supplier) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.minimumStock = minimumStock;
        this.supplier = supplier;
    }

    public Product(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public double getBuyPrice() { return buyPrice; }

    public double getSellPrice() { return sellPrice; }

    public int getMinimumStock() { return minimumStock; }

    public Supplier getSupplier() { return supplier; }

}