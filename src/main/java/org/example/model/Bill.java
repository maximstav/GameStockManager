package org.example.model;

import java.sql.Timestamp;

// immutable class, corresponds to the Log DB table
public record Bill(int id, int orderId, String billText, Timestamp createdAt) {

    @Override
    public Timestamp createdAt() {
        return new Timestamp(createdAt.getTime());
    }
}
