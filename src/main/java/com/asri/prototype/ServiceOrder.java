package com.asri.prototype;

public class ServiceOrder {
    private int id;
    private int customerId;
    private String customerName;
    private String serviceName;
    private double price;
    private String status;
    private String priority;
    private String startDate;
    private String endDate;
    private String description;

    public ServiceOrder() {}

    public ServiceOrder(int id, int customerId, String customerName, String serviceName,
                        double price, String status, String priority,
                        String startDate, String endDate, String description) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.serviceName = serviceName;
        this.price = price;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getServiceName() { return serviceName; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setDescription(String description) { this.description = description; }
}
