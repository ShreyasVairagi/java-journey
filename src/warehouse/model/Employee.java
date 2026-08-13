package warehouse.model;

public class Employee {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Role role;

    // Constructor for creating new records (before DB generates an ID)
    public Employee(String name, String email, String phone, String address, Role role){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    // Constructor for existing records retrieved from DB
    public Employee(int id, String name, String email, String phone, String address, Role role){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public int getId() {return id;}

    public String getName() {return name;}

    public String getEmail() {return email;}

    public String getPhone() {return phone;}

    public String getAddress() {return address;}

    public Role getRole() {return role;}
}
