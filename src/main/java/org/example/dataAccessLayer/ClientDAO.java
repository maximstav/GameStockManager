package org.example.dataAccessLayer;

import org.example.connection.ConnectionFactory;
import org.example.model.Client;
import java.sql.*;
import java.util.logging.*;

public class ClientDAO extends AbstractDAO<Client> {
    private static final Logger LOGGER = Logger.getLogger(ClientDAO.class.getName());

    /**
     * Finds a client by username
     * @param username The username to search for
     * @return The Client if found, null otherwise
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
     * Deletes a client by id
     * @param id The client id to delete
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM client WHERE id = ?";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    @Override
    protected String getTableName() {
        return "client"; // Direct table name since MySQL table names are case-sensitive
    }
}