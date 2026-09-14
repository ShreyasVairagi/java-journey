package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import warehouse.managers.InventoryManager;



class InventoryManagerTest {

    @Test
    void addNum() {
        InventoryManager im = new InventoryManager();
        assertEquals(2, im.addNum(1,1));
    }
}