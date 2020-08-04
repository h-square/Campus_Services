package com.example.campus_services;

import java.util.ArrayList;

public class Order {
    private String OrderNo;
    private String CustomerId;
    private String CanteenName;
    private String OrderDetails;
    private String OrderDateTime;
    private ArrayList<String> CookingInstruction;
    private String PaymentMethod;
    private String Status;

    public Order(){

    }

    public Order(String orderNo, String customerId, String canteenName, String orderDetails, String orderDateTime, ArrayList<String> cookingInstruction, String paymentMethod, String status) {
        OrderNo = orderNo;
        CustomerId = customerId;
        CanteenName = canteenName;
        OrderDetails = orderDetails;
        OrderDateTime = orderDateTime;
        CookingInstruction = cookingInstruction;
        PaymentMethod = paymentMethod;
        Status = status;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public ArrayList<String> getCookingInstruction() {
        return CookingInstruction;
    }

    public void setCookingInstruction(ArrayList<String> cookingInstruction) {
        CookingInstruction = cookingInstruction;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCanteenName() {
        return CanteenName;
    }

    public void setCanteenName(String canteenName) {
        CanteenName = canteenName;
    }

    public String getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        OrderDetails = orderDetails;
    }

    public String getOrderDateTime() {
        return OrderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        OrderDateTime = orderDateTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status){
        Status = status;
    }
}
