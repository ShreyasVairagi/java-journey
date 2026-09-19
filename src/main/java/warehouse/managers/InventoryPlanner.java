package warehouse.managers;

import warehouse.model.InventoryStock;
import java.util.ArrayList;
import java.util.List;

public class InventoryPlanner {

    public static int calculateTotalStock(List<InventoryStock> stockList) {
        int totalAvailableStock = 0;

        for (InventoryStock stock : stockList) {
            totalAvailableStock += stock.getQuantity();
        }

        return totalAvailableStock;
    }

    public static class StockAdjustmentAction {
        private final String locationId;
        private final int quantityChange;
        private final boolean removeLocation;
        private final boolean addLocation;

        public StockAdjustmentAction(String locationId, int quantityChange, boolean removeLocation) {
            this.locationId = locationId;
            this.quantityChange = quantityChange;
            this.removeLocation = removeLocation;
            this.addLocation = false;
        }

        public StockAdjustmentAction(String locationId, int quantityChange, boolean removeLocation, boolean addLocation) {
            this.locationId = locationId;
            this.quantityChange = quantityChange;
            this.removeLocation = removeLocation;
            this.addLocation = addLocation;
        }

        public String getLocationId() { return locationId; }
        public int getQuantityChange() { return quantityChange; }
        public boolean removeLocation() { return removeLocation; }
        public boolean addLocation() { return addLocation; }
    }

    public static List<StockAdjustmentAction> stockAddition(List<InventoryStock> existingQuantities, int quantityToAdd, String overflowLocationId) {
        List<StockAdjustmentAction> actions = new ArrayList<>();
        int remainingQuantity = quantityToAdd;

        for (InventoryStock currentStock : existingQuantities) {
            if (remainingQuantity <= 0) break;

            int dbQuantity = currentStock.getQuantity();
            String productLocation = currentStock.getLocation().getLocationID();
            int spaceAvailable = 100 - dbQuantity;

            if (spaceAvailable > 0) {
                int amountToAdd = Math.min(spaceAvailable, remainingQuantity);
                actions.add(new StockAdjustmentAction(productLocation, amountToAdd, false));
                remainingQuantity -= amountToAdd;
            }
        }

        // overflow/new location
        if (remainingQuantity > 0) {
            if (overflowLocationId == null || overflowLocationId.trim().isEmpty()) {
                throw new IllegalStateException("OVERFLOW_LOCATION_REQUIRED:" + remainingQuantity);
            }

            actions.add(new StockAdjustmentAction(overflowLocationId, remainingQuantity, false, true));
        }

        return actions;
    }

    public static List<StockAdjustmentAction> stockReduction(List<InventoryStock> existingQuantities, int removalQuantity) {
        List<StockAdjustmentAction> actions = new ArrayList<>();
        int inputConversion = removalQuantity;

        for (int i = existingQuantities.size() - 1; i >= 0; i--) {
            InventoryStock currentStock = existingQuantities.get(i);
            int dbQuantity = currentStock.getQuantity();
            String productLocation = currentStock.getLocation().getLocationID();

            if (dbQuantity > inputConversion) {
                actions.add(new StockAdjustmentAction(productLocation, -inputConversion, false));
                inputConversion = 0;
            } else {
                actions.add(new StockAdjustmentAction(productLocation, 0, true));
                inputConversion -= dbQuantity;
            }

            if (inputConversion == 0) {
                break;
            }
        }
        return actions;
    }

    public static InventoryStock findAndValidateSourceStock(List<InventoryStock> stockList, String sourceLocationId, int quantity) {
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
}