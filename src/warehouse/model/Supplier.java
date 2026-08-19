package warehouse.model;

public class Supplier {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Supplier(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Supplier(int id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Supplier(int supplierid) {
        this.id = supplierid;
    }

    public int getId() {return id;}

    public String getName() {return name;}

    public String getAddress() {return address;}

    public String getPhone() {return phone;}

    public String getEmail() {return email;}
}
