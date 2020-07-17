package com.example.campus_services;

import android.content.Context;
import android.widget.ImageView;

public class Canteen {
    private String Name;
    private String Email;
    private String Available;
    private String Contact;
    private String Virtual_Money;
    private String Image_Url;
    private boolean Ban;

    public Canteen(){

    }

    public Canteen(String available, String email, String name,String contact,String virtual_Money) {
        Name = name;
        Email = email;
        Available = available;
        Contact = contact;
        Virtual_Money = virtual_Money;
        Image_Url = "";
        this.Ban = false;
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

    public String getImage_Url() { return Image_Url; }

    public String getContact() { return Contact; }

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

    public void setImage_Url(String image_Url) { Image_Url = image_Url; }

    public void setContact(String contact) { Contact = contact; }

    public void setBan(boolean ban) { Ban = ban; }

    public boolean isBan() { return Ban; }
}
