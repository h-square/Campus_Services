package com.example.campus_services;

public class Supervisior
{
    private String Name;
    private String Email;
    private String Contact;
    private int Slot;
    private String Image_Url;
    private boolean Ban;

    public Supervisior() {
    }

    public Supervisior(String name, String email, String contact, int slot) {
        Name = name;
        Email = email;
        Contact = contact;
        Slot = slot;
        this.Ban = false;
        this.Image_Url = "";
    }

    public String getName() { return Name; }

    public String getEmail() { return Email; }

    public void setImage_Url(String image_Url) { Image_Url = image_Url; }

    public String getImage_Url() { return Image_Url; }

    public String getContact() { return Contact; }

    public int getSlot() { return Slot; }

    public void setName(String name) { Name = name; }

    public void setEmail(String email) { Email = email; }

    public void setContact(String contact) { Contact = contact; }

    public void setSlot(int slot) { Slot = slot; }

    public void setBan(boolean ban) { Ban = ban; }

    public boolean isBan() { return Ban; }
}
