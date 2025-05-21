package org.example.dataAccessLayer;

import org.example.connection.ConnectionFactory;
import org.example.model.Client;
import java.sql.*;
import java.util.logging.*;

/**
 * ClientDAO is a DAO class for managing Client entities.
 * It extends AbstractDAO and adds specific methods for client operations.
 */
public class ClientDAO extends AbstractDAO<Client> {
    private static final Logger LOGGER = Logger.getLogger(ClientDAO.class.getName());

    /**
     * Finds a client by their username.
     *
     * @param username the username to search for
     * @return the Client object if found, or null if not found
     */

    public Client findByUsername(String username) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM client WHERE username = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fetchedUsername = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");

                return new Client(id, fetchedUsername, password, role);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:findByUsername " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * Returns the name of the client table.
     *
     * @return "client"
     */
    @Override
    protected String getTableName() {
        return "client"; // Direct table name since MySQL table names are case-sensitive
    }
}