package com.example.campus_services;

public class User {
    private String Name;
    private String Contact;
    private String Virtual_Money;

    public User(){

    }

    public User(String name, String contact, String vm){
        Name = name;
        Contact = contact;
        Virtual_Money = vm;
    }

    public String getName(){
        return Name;
    }

    public void setName(String nam){
        Name = nam;
    }

    public String getContact(){
        return Contact;
    }

    public void setContact(String nu){
        Contact=nu;
    }

    public String getVirtual_Money(){
        return Virtual_Money;
    }

    public void setVirtual_Money(String mo){
        Virtual_Money=mo;
    }
}
