package com.asri.prototype;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String customerType;
    private String notes;

    public Customer() {}

    public Customer(int id, String name, String phone, String email, String address, String customerType, String notes) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.customerType = customerType;
        this.notes = notes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getCustomerType() { return customerType; }
    public String getNotes() { return notes; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }
    public void setNotes(String notes) { this.notes = notes; }
}
