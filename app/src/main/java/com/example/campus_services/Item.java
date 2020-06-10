package com.example.campus_services;

public class Item {
    private String Name;
    private String Price;
    private String Availability;

    public Item(){

    }

    public Item(String name, String price, String availability) {
        Name = name;
        Price = price;
        Availability = availability;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public String getAvailability() {
        return Availability;
    }
}
