package com.example.campus_services;

public class Order {
    private String OrderNo;
    private String CustomerId;
    private String CanteenName;
    private String OrderDetails;
    private String OrderDateTime;
    private String CookingInstruction;
    private String PaymentMethod;

    public Order(){

    }

    public Order(String orderNo, String customerId, String canteenName, String orderDetails, String orderDateTime, String cookingInstruction, String paymentMethod) {
        OrderNo = orderNo;
        CustomerId = customerId;
        CanteenName = canteenName;
        OrderDetails = orderDetails;
        OrderDateTime = orderDateTime;
        CookingInstruction = cookingInstruction;
        PaymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getCookingInstruction() {
        return CookingInstruction;
    }

    public void setCookingInstruction(String cookingInstruction) {
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
}
