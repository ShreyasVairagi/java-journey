package test;

import org.junit.jupiter.api.Test;
import warehouse.managers.InventoryManager;
import warehouse.managers.InventoryPlanner;
import warehouse.model.InventoryStock;
import warehouse.model.StorageLocation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryPlannerTest {

    // Calculate Total Stock

    @Test
    void testCalculateTotalStockWithMultipleBins() {
        List<InventoryStock> mockStockList = new ArrayList<>();
        mockStockList.add(new InventoryStock(null, null, 40));
        mockStockList.add(new InventoryStock(null, null, 60));

        int total = InventoryPlanner.calculateTotalStock(mockStockList);
        assertEquals(100, total);
    }

    @Test
    void testCalculateTotalStockWithOneBin() {
        List<InventoryStock> mockStockList = new ArrayList<>();
        mockStockList.add(new InventoryStock(null, null, 60));

        int total = InventoryPlanner.calculateTotalStock(mockStockList);
        assertEquals(60, total);
    }

    @Test
    void testCalculateTotalStockWithEmptyList() {
        List<InventoryStock> mockStockList = new ArrayList<>();

        int total = InventoryPlanner.calculateTotalStock(mockStockList);
        assertEquals(0, total);
    }

    //Stock Addition

    @Test
    void testStockAddition() {
        StorageLocation loc1 = new StorageLocation("LOC-A", 1, 'A', 1, 100);

        List<InventoryStock> mockStockList = new ArrayList<>();
        mockStockList.add(new InventoryStock(null, loc1, 40));

        List<InventoryPlanner.StockAdjustmentAction> actions = InventoryPlanner.stockAddition(mockStockList, 10, null);

        assertEquals("LOC-A", actions.get(0).getLocationId());
        assertEquals(10, actions.get(0).getQuantityChange());
        assertFalse(actions.get(0).addLocation(), "Should be an update, not a new location addition");
    }

    @Test
    void testStockAddition_specificLocationAdd() {
        StorageLocation loc1 = new StorageLocation("LOC-A", 1, 'A', 1, 100);
        StorageLocation loc2 = new StorageLocation("LOC-B", 1, 'B', 1, 100);
        StorageLocation loc3 = new StorageLocation("LOC-C", 1, 'C', 1, 100);

        List<InventoryStock> mockStockList = new ArrayList<>();
        mockStockList.add(new InventoryStock(null, loc1, 100));
        mockStockList.add(new InventoryStock(null, loc2, 100));
        mockStockList.add(new InventoryStock(null, loc3, 80));

        List<InventoryPlanner.StockAdjustmentAction> actions = InventoryPlanner.stockAddition(mockStockList, 10, "LOC-C");

        assertEquals("LOC-C", actions.get(0).getLocationId());
        assertEquals(10, actions.get(0).getQuantityChange());
        assertFalse(actions.get(0).addLocation());
    }

    @Test
    void testStockAddition_overflowTest() {
        StorageLocation loc1 = new StorageLocation("LOC-A", 1, 'A', 1, 100);
        StorageLocation loc2 = new StorageLocation("LOC-B", 1, 'B', 1, 100);
        StorageLocation loc3 = new StorageLocation("LOC-C", 1, 'C', 1, 100);

        List<InventoryStock> mockStockList = new ArrayList<>();
        mockStockList.add(new InventoryStock(null, loc1, 100));
        mockStockList.add(new InventoryStock(null, loc2, 90));
        mockStockList.add(new InventoryStock(null, loc3, 80));

        List<InventoryPlanner.StockAdjustmentAction> actions = InventoryPlanner.stockAddition(mockStockList, 30, null);

        assertEquals(2, actions.size());

        assertEquals("LOC-B", actions.get(0).getLocationId());
        assertEquals(10, actions.get(0).getQuantityChange());
        assertFalse(actions.get(0).addLocation());

        assertEquals("LOC-C", actions.get(1).getLocationId());
        assertEquals(20, actions.get(1).getQuantityChange());
        assertFalse(actions.get(1).addLocation());
    }

    @Test
    void testStockAddition_newLocation() {
        List<InventoryStock> mockStockList = new ArrayList<>();

        List<InventoryPlanner.StockAdjustmentAction> actions = InventoryPlanner.stockAddition(mockStockList, 50, "LOC-A");

        assertEquals(1, actions.size());
        assertEquals("LOC-A", actions.get(0).getLocationId());
        assertEquals(50, actions.get(0).getQuantityChange());
        assertTrue(actions.get(0).addLocation());
    }

    // Stock Reduction

    @Test
    void testStockReduction() {
        StorageLocation loc1 = new StorageLocation("LOC-A", 1, 'A', 1, 100);
        StorageLocation loc2 = new StorageLocation("LOC-B", 1, 'B', 1, 100);

        List<InventoryStock> mockStockList = new ArrayList<>();
        mockStockList.add(new InventoryStock(null, loc1, 40));
        mockStockList.add(new InventoryStock(null, loc2, 30));

        List<InventoryPlanner.StockAdjustmentAction> actions = InventoryPlanner.stockReduction(mockStockList, 50);

        assertEquals(2, actions.size());
        assertEquals("LOC-B", actions.get(0).getLocationId());
        assertTrue(actions.get(0).removeLocation());
        assertEquals("LOC-A", actions.get(1).getLocationId());
        assertEquals(-20, actions.get(1).getQuantityChange());
    }

    // Stock Reduction

    @Test
    void testFindAndValidateSourceStock() {
        StorageLocation loc1 = new StorageLocation("LOC-A", 1, 'A', 1, 100);
        List<InventoryStock> mockStockList = new ArrayList<>();
        mockStockList.add(new InventoryStock(null, loc1, 40));

        InventoryStock found = InventoryPlanner.findAndValidateSourceStock(mockStockList, "LOC-A", 20);
        assertNotNull(found);
        assertEquals("LOC-A", found.getLocation().getLocationID());
    }


}