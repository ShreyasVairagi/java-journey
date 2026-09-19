package warehouse.managers;

import warehouse.dao.*;
import warehouse.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private final InventoryStockDAO inventoryStockDAO;
    private final ProductDAO productDAO;
    private final EmployeeDAO employeeDAO;
    private final TransactionDAO transactionDAO;
    private final SupplierDAO supplierDAO;

    public InventoryManager(InventoryStockDAO inventoryStockDAO,
                            ProductDAO productDAO,
                            EmployeeDAO employeeDAO,
                            TransactionDAO transactionDAO, SupplierDAO supplierDAO) {
        this.inventoryStockDAO = inventoryStockDAO;
        this.productDAO = productDAO;
        this.employeeDAO = employeeDAO;
        this.transactionDAO = transactionDAO;
        this.supplierDAO = supplierDAO;
    }

    public void addProduct(Product product){
        productDAO.addProduct(product);
    }

    //positive number to add or negative number to reduce stock
    //refactor with removing exception communication
    public void adjustStock(Product product, int quantity, int employeeId, String locationId) {
        if (quantity == 0) {
            throw new IllegalArgumentException("Quantity cannot be zero. Stock adjustment cancelled.");
        }

        Employee employee = employeeDAO.findSingleEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found! Stock adjustment cancelled.");
        }

        Product existingProduct = productDAO.findSingleProduct(product.getId());
        if (existingProduct == null) {
            throw new IllegalArgumentException("Error: Product with ID " + product.getId() + " does not exist. Stock adjustment cancelled.");
        }

        List<InventoryStock> existingQuantities = inventoryStockDAO.findStockForProduct(product);

        if (quantity < 0) {
            int totalAvailableStock = InventoryPlanner.calculateTotalStock(existingQuantities);
            int requestedRemoval = Math.abs(quantity);
            if (totalAvailableStock < requestedRemoval) {
                throw new IllegalArgumentException("Error: Cannot remove " + requestedRemoval + " items. Total available stock across all locations is only " + totalAvailableStock + ". Adjustment cancelled.");
            }
        }

        // Handle positive value (Adding stock)
        if (quantity > 0) {
            if (existingQuantities.isEmpty() && (locationId == null || locationId.trim().isEmpty())) {
                throw new IllegalArgumentException("Error: Location ID is required for new stock. Adjustment cancelled.");
            }

            // Delegate to InventoryPlanner
            List<InventoryPlanner.StockAdjustmentAction> actions = InventoryPlanner.stockAddition(existingQuantities, quantity, locationId);

            for (InventoryPlanner.StockAdjustmentAction action : actions) {
                int productID = product.getId();
                String locId = action.getLocationId();
                int qtyChange = action.getQuantityChange();

                int currentBinTotal = inventoryStockDAO.getTotalStockAtLocation(locId);
                int maxCapacity = inventoryStockDAO.getLocationCapacity(locId);

                if ((currentBinTotal + qtyChange) > maxCapacity) {
                    int availableSpace = maxCapacity - currentBinTotal;
                    throw new IllegalArgumentException("Error: Location " + locId + " cannot fit " + qtyChange +
                            " items. Total physical space remaining in this bin is only " + Math.max(0, availableSpace) +
                            " (shared with other products). Adjustment cancelled.");
                }

                if (action.addLocation()) {
                    boolean success = inventoryStockDAO.addProductToLocation(productID, locId, qtyChange);
                    if (!success) {
                        throw new IllegalArgumentException("Adjustment cancelled due to database error during stock addition.");
                    }
                } else {
                    inventoryStockDAO.updateQuantity(productID, locId, qtyChange);
                }
            }
        }
        // Handle negative value
        else {
            int inputConversion = Math.abs(quantity);

            // Delegate to InventoryPlanner
            List<InventoryPlanner.StockAdjustmentAction> actions = InventoryPlanner.stockReduction(existingQuantities, inputConversion);

            for (InventoryPlanner.StockAdjustmentAction action : actions) {
                int productID = product.getId();
                if (action.removeLocation()) {
                    inventoryStockDAO.removeLocation(productID, action.getLocationId());
                } else {
                    inventoryStockDAO.updateQuantity(productID, action.getLocationId(), action.getQuantityChange());
                }
            }
        }

        TransactionType type = (quantity > 0) ? TransactionType.IN : TransactionType.OUT;
        int transactionQuantity = Math.abs(quantity);
        Transaction transaction = new Transaction(product, employee, transactionQuantity, type, LocalDate.now(), LocalTime.now());
        transactionDAO.add(transaction);
    }

    // moveProduct helper
    private static InventoryStock findAndValidateSourceStock(List<InventoryStock> stockList, String sourceLocationId, int quantity) {
        InventoryStock sourceStock = null;
        for (InventoryStock stock : stockList) {
            if (stock.getLocation().getLocationID().equalsIgnoreCase(sourceLocationId)) {
                sourceStock = stock;
                break;
            }
        }

        if (sourceStock == null) {
            throw new IllegalArgumentException("Error: Source location ID '" + sourceLocationId + "' not found for this product.");
        }

        if (sourceStock.getQuantity() < quantity) {
            throw new IllegalArgumentException("Error: Source location only contains " + sourceStock.getQuantity() +
                    " items, but you tried to move " + quantity + ".");
        }

        return sourceStock;
    }

    public void moveProduct(Product product, int quantity, Employee employee, String sourceLocationId, String destinationLocationId) {
        if (product == null || employee == null) {
            throw new IllegalArgumentException("Product or Employee cannot be null. Move cancelled.");
        }

        List<InventoryStock> stockList = inventoryStockDAO.findStockForProduct(product);
        if (!inventoryStockDAO.locationExists(destinationLocationId)) {
            throw new IllegalArgumentException("Error: Destination location ID does not exist.");
        }

        // Delegate to InventoryPlanner
        InventoryStock sourceStock = InventoryPlanner.findAndValidateSourceStock(stockList, sourceLocationId, quantity);

        int currentDestinationQty = inventoryStockDAO.getQuantityForProductAndLocation(product.getId(), destinationLocationId);
        if ((100 - currentDestinationQty) < quantity) {
            throw new IllegalStateException("Error: Destination location is too full to accept " + quantity + " items.");
        }

        boolean added = inventoryStockDAO.addProductToLocation(product.getId(), destinationLocationId, quantity);
        if (!added) {
            throw new RuntimeException("Failed to add product to destination location.");
        }

        inventoryStockDAO.updateQuantity(product.getId(), sourceLocationId, -quantity);

        if (sourceStock.getQuantity() - quantity == 0) {
            inventoryStockDAO.removeLocation(product.getId(), sourceLocationId);
        }

        Transaction transaction = new Transaction(product, employee, quantity, TransactionType.MOVE, LocalDate.now(), LocalTime.now());
        transactionDAO.add(transaction);
    }
    public void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Error: Product does not exist. Removal cancelled.");
        }

        inventoryStockDAO.removeLocation(product.getId());

        boolean success = productDAO.deleteProduct(product.getId());
        if (!success) {
            throw new RuntimeException("Failed to remove product from the database. Removal cancelled.");
        }
    }

    public Product findProduct(int productId) {
        Product foundProduct = productDAO.findSingleProduct(productId);
        if (foundProduct == null) {
            throw new IllegalArgumentException("Error: Product with ID " + productId + " not found.");
        }
        return foundProduct;
    }

    public static class LowStockItem {
        private final Product product;
        private final int totalQuantity;

        public LowStockItem(Product product, int totalQuantity) {
            this.product = product;
            this.totalQuantity = totalQuantity;
        }

        public Product getProduct() { return product; }
        public int getTotalQuantity() { return totalQuantity; }
    }

    public List<LowStockItem> getLowStockProducts() {
        List<Product> listOfProducts = productDAO.viewAllProducts();
        List<LowStockItem> lowStockItems = new ArrayList<>();

        for (Product product : listOfProducts) {
            List<InventoryStock> stockList = inventoryStockDAO.findStockForProduct(product);

            int totalQuantity = InventoryPlanner.calculateTotalStock(stockList);

            if (totalQuantity <= product.getMinimumStock()) {
                lowStockItems.add(new LowStockItem(product, totalQuantity));
            }
        }

        return lowStockItems;
    }

    public double calculateInventoryValue() {
        return inventoryStockDAO.getTotalInventoryValue();
    }

    public List<Transaction> getProductTransactionHistory(int productId) {
        return transactionDAO.getProductTransactionHistory(productId);
    }

    public List<InventoryStock> viewStockForProduct(int productId) {
        return inventoryStockDAO.findStockForProduct(new Product(productId));
    }

//    public List<InventoryStock> findStockForProduct(Product product) {
//        return inventoryStockDAO.findStockForProduct(product);
//    }

    public boolean locationExists(String locationId) {
        return inventoryStockDAO.locationExists(locationId);
    }

    public boolean supplierExists(int supplierId) {
        return supplierDAO.findSingleSupplier(supplierId) != null;
    }
}