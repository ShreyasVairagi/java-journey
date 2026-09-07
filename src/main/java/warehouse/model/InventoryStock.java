package warehouse.model;

public class InventoryStock {
    private Product product;
    private StorageLocation location;
    private int quantity;

    public InventoryStock(Product product, StorageLocation location, int quantity) {
        this.product = product;
        this.location = location;
        this.quantity = quantity;
    }

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}

    public StorageLocation getLocation() {return location;}

    public void setLocation(StorageLocation location) {this.location = location;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}
}
