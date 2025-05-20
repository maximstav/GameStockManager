package org.example.model;

import java.sql.Timestamp;

// immutable class, corresponds to the Log DB table
public class Bill {
    private int id;  // modificabil
    private final int orderId;
    private final String billText;
    private final Timestamp createdAt;

    public Bill(int id, int orderId, String billText, Timestamp createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.billText = billText;
        this.createdAt = createdAt;
    }

    public Timestamp createdAt() {
        return new Timestamp(createdAt.getTime());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getBillText() {
        return billText;
    }
}
