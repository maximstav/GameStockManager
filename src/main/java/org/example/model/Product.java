package org.example.model;

import java.math.BigDecimal;

/**
 * Represents a Product available in the warehouse.
 * <p>
 * Contains the product's unique ID, name, current quantity in stock,
 * and price per unit.
 * Maps to the Product table in the database.
 *
 * @author Staver Maxim
 * @version 1.0
 */
public class Product {
    private int id;
    private String name;
    private int quantity;
    private BigDecimal price;

    /**
     * Constructs a new Product instance with the specified details.
     *
     * @param id the unique product ID
     * @param name the name of the product
     * @param quantity the current stock quantity
     * @param price the price per unit
     */
    public Product(int id, String name, int quantity, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price.floatValue();
    }

    public BigDecimal getBigDecimalPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
