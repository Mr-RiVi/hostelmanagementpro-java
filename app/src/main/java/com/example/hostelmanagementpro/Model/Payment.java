package com.example.hostelmanagementpro.Model;

public class Payment {
    String id,Name,Month,Price;


    public Payment() {
    }

    public Payment(String id, String name, String month, String price) {
        this.id = id;
        Name = name;
        Month = month;
        Price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
