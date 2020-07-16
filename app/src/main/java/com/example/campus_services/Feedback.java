package com.example.campus_services;

public class Feedback {
    private String CanteenName;
    private String Message;

    public Feedback(){

    }

    public Feedback(String canteenName, String message) {
        CanteenName = canteenName;
        Message = message;
    }

    public String getCanteenName() {
        return CanteenName;
    }

    public void setCanteenName(String canteenName) {
        CanteenName = canteenName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
