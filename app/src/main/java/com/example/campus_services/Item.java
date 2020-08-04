package com.example.campus_services;

import java.util.ArrayList;

public class Item {
    private String Name;
    private String Price;
    private String Availability;
    private ArrayList<String> Instructions;

    public Item(){

    }

    public Item(String name, String price, String availability, ArrayList<String> instructions) {
        Name = name;
        Price = price;
        Availability = availability;
        Instructions = instructions;
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

    public void setInstructions(ArrayList<String> instructions){
        Instructions = instructions;
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

    public ArrayList<String> getInstructions(){
        return Instructions;
    }
}
