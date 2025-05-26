package org.example.model;

import java.sql.Timestamp;

/**
 * Represents a Bill generated for an Order.
 * <p>
 * This class corresponds to the Log database table and is intended to be immutable
 * except for the {@code id} field, which can be modified.
 * Each Bill contains an identifier, the associated order ID, the textual
 * representation of the bill, and the creation timestamp.
 *
 * Bills are created when an order is finalized and stored for logging purposes.
 *
 * @author Staver Maxim
 * @version 1.0
 */
// immutable class, corresponds to the Log DB table
import java.sql.Timestamp;

import java.sql.Timestamp;

public record Bill(int id, int orderId, String billText, Timestamp createdAt) {

    public Bill {
        createdAt = new Timestamp(createdAt.getTime());
    }

    public Timestamp getCreatedAt() {
        return new Timestamp(createdAt.getTime());
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


//public class Bill {
//    private int id;  // modificabil
//    private final int orderId;
//    private final String billText;
//    private final Timestamp createdAt;
//
//    /**
//     * Constructs a new Bill instance with the specified properties.
//     *
//     * @param id the bill's unique identifier
//     * @param orderId the ID of the related order
//     * @param billText the textual representation of the bill
//     * @param createdAt the creation timestamp of the bill
//     */
//    public Bill(int id, int orderId, String billText, Timestamp createdAt) {
//        this.id = id;
//        this.orderId = orderId;
//        this.billText = billText;
//        this.createdAt = createdAt;
//    }
//
//    public Timestamp createdAt() {
//        return new Timestamp(createdAt.getTime());
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public int getOrderId() {
//        return orderId;
//    }
//
//    public String getBillText() {
//        return billText;
//    }
//
//    public Timestamp getCreatedAt() {
//        return createdAt;
//    }
//}
