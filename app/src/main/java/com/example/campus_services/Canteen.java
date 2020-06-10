package com.example.campus_services;

public class Canteen {
    private String Name;
    private String Email;
    private String Available;
    private String Virtual_Money;

    public Canteen(){

    }

    public Canteen(String available, String email, String name, String virtual_Money) {
        Name = name;
        Email = email;
        Available = available;
        Virtual_Money = virtual_Money;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getAvailable() {
        return Available;
    }

    public String getVirtual_Money() {
        return Virtual_Money;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public void setVirtual_Money(String virtual_Money) {
        Virtual_Money = virtual_Money;
    }
}
