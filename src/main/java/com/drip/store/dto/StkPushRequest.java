package com.drip.store.dto;

public class StkPushRequest {

    private String phone;    // Customer phone number
    private int    amount;   // Amount in KES
    private String orderId;  // Your internal order ID

    public StkPushRequest() {}

    public StkPushRequest(String phone, int amount, String orderId) {
        this.phone   = phone;
        this.amount  = amount;
        this.orderId = orderId;
    }

    public String getPhone()              { return phone;   }
    public void   setPhone(String phone)  { this.phone = phone; }

    public int    getAmount()             { return amount;  }
    public void   setAmount(int amount)   { this.amount = amount; }

    public String getOrderId()            { return orderId; }
    public void   setOrderId(String id)   { this.orderId = id; }
}