package warehouse.model;

public class StorageLocation {
    private String locationID;
    private int aisle;
    private char shelf;
    private int bin;
    private int capacity;

    public StorageLocation(String locationID, int aisle, char shelf, int bin, int capacity) {
        this.locationID = locationID;
        this.aisle = aisle;
        this.shelf = shelf;
        this.bin = bin;
        this.capacity = capacity;
    }

    public String getLocationID() {return locationID;}

    public int getAisle() {return aisle;}

    public char getShelf() {return shelf;}

    public int getBin() {return bin;}

    public int getCapacity() {return capacity;}


}
