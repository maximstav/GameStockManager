package org.example.model;

/**
 * Represents a Client in the Orders Management system.
 * <p>
 * Contains client identification, username, email address, and role.
 * The role can be "ADMIN" or "CLIENT" to determine user permissions.
 * Instances of this class map to the Client table in the database.
 *
 * @author Staver Maxim
 * @version 1.0
 */
public class Client {
    private int id;
    private String username;
    private String email;
    private String role; // "ADMIN" or "CLIENT"

    /**
     * Constructs a new Client instance with specified parameters.
     *
     * @param id the unique client ID
     * @param username the username of the client
     * @param email the email address of the client
     * @param role the role of the client ("ADMIN" or "CLIENT")
     */
    public Client(int id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}