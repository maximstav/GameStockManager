package org.example.bussinessLayer;

import org.example.dataAccessLayer.ClientDAO;
import org.example.model.Client;

import java.util.List;

public class ClientService {
    private final ClientDAO clientDAO = new ClientDAO();

    public Client authenticate(String username, String password) {
        Client client = clientDAO.findByUsername(username);
        if (client != null && client.getPassword().equals(password)) {
            return client;
        }
        return null;
    }

    // Note: Role must be either 'admin' or 'client' according to DB schema
    public Client registerClient(Client client) {
        // maybe check if username exists
        return clientDAO.insert(client);
    }

    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }

    public boolean deleteClient(int id) {
        return clientDAO.delete(id);
    }

    public Client getClientById(int id) {
        return clientDAO.findById(id);
    }

    public Client updateClient(Client client) {
        return clientDAO.update(client);
    }
}