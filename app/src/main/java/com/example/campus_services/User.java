package com.example.campus_services;

public class User {
    private String Name;
    private String Contact;
    private String Virtual_Money;
    private String Image_Url;
    private boolean Ban;


    public User(){ }

    public User(String name, String contact, String vm){
        this.Name = name;
        this.Contact = contact;
        this.Virtual_Money = vm;
        this.Image_Url = "";
        this.Ban = false;
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

    public String getImage_Url() { return Image_Url; }

    public void setImage_Url(String image_Url) { Image_Url = image_Url; }


    public void setBan(boolean ban) { Ban = ban; }

    public boolean isBan() { return Ban; }

}
