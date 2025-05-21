package org.example.model;

import java.sql.Timestamp;

/**
 * Represents an Order placed by a Client for a Product.
 * <p>
 * Contains information about the order including IDs for the order,
 * client, and product, the quantity ordered, and the timestamp when
 * the order was created.
 *
 * Maps to the Order table in the database.
 *
 * @author Staver Maxim
 * @version 1.0
 */
public class Order {
    private int id;
    private int client_id;
    private int product_id;
    private int quantity;
    private Timestamp timestamp;

    /**
     * Constructs a new Order instance with the specified details.
     *
     * @param id the unique order ID
     * @param client_id the client ID placing the order
     * @param product_id the product ID being ordered
     * @param quantity the quantity of the product ordered
     * @param timestamp the timestamp when the order was created
     */
    public Order(int id, int client_id, int product_id, int quantity, Timestamp timestamp) {
        this.id = id;
        this.client_id = client_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
