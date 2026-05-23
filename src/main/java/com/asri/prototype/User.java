package com.asri.prototype;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String role;
    private String gender;
    private String address;
    private String securityQuestion;

    public User() {}

    public User(int id, String fullName, String email, String phone, String dateOfBirth,
                String role, String gender, String address, String securityQuestion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.gender = gender;
        this.address = address;
        this.securityQuestion = securityQuestion;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getRole() { return role; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getSecurityQuestion() { return securityQuestion; }

    public void setId(int id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setRole(String role) { this.role = role; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }
}
