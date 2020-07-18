package com.example.campus_services;

public class Doctor {
    private String Name;
    private String Email;
    private String Conatct;
    private String Slot;
    private boolean Ban;

    public Doctor() {
    }

    public Doctor(String name, String email, String conatct, String slot) {
        Name = name;
        Email = email;
        Conatct = conatct;
        Slot = slot;
        this.Ban = false;
    }

    public String getName() { return Name; }

    public String getEmail() { return Email; }

    public String getConatct() { return Conatct; }

    public String getSlot() { return Slot; }

    public void setName(String name) { Name = name; }

    public void setEmail(String email) { Email = email; }

    public void setConatct(String conatct) { Conatct = conatct; }

    public void setSlot(String slot) { Slot = slot; }

    public void setBan(boolean ban) { Ban = ban; }

    public boolean isBan() { return Ban; }


}
