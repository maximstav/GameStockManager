    package org.example.bussinessLayer;

    import org.example.dataAccessLayer.ClientDAO;
    import org.example.model.Client;

    import java.util.List;

    /**
     * Service class for managing clients, including authentication and registration.
     */
    public class ClientService {
        private final ClientDAO clientDAO = new ClientDAO();

        /**
         * Authenticates a client using username and email.
         *
         * @param username the client's username
         * @param email the client's email
         * @return the authenticated {@link Client}, or null if authentication fails
         */
        public Client authenticate(String username, String email) {
            Client client = clientDAO.findByUsername(username);
            if (client != null && client.getEmail().equals(email)) {
                return client;
            }
            return null;
        }

        /**
         * Registers a new client.
         *
         * @param client the {@link Client} object to register
         * @return the inserted {@link Client} object
         */
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