package org.example.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.bussinessLayer.ClientService;
import org.example.model.Client;

/**
 * JavaFX controller for managing clients.
 * Handles adding, updating, and deleting clients via the GUI.
 */
public class ClientController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button addClientButton;

    @FXML
    private Button updateClientButton;

    @FXML
    private Button deleteClientButton;


    @FXML
    private TableView<Client> clientTable;

    private final ClientService clientService = new ClientService();
    private final org.example.dataAccessLayer.ClientDAO clientDAO = new org.example.dataAccessLayer.ClientDAO();


    /**
     * Initializes the controller after the FXML elements are loaded.
     * Sets up table columns and loads all clients from the database.
     */
    @FXML
    public void initialize() {
        // Use the generic DAO's TableView setup
        TableView<Client> tableView = clientDAO.getTableView();
        clientTable.getColumns().setAll(tableView.getColumns());
        clientTable.setItems(tableView.getItems());

        addClientButton.setOnAction(event -> handleAddClient());
        updateClientButton.setOnAction(event -> handleUpdateClient());
        deleteClientButton.setOnAction(event -> handleDeleteClient());

        clientTable.setOnMouseClicked(event -> {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                nameField.setText(selectedClient.getUsername());
                emailField.setText(selectedClient.getEmail());
            }
        });
    }

    private void refreshTable() {
        TableView<Client> tableView = clientDAO.getTableView();
        clientTable.setItems(tableView.getItems());
    }


    /**
     * Handles adding a new client using input from the text fields.
     */
    private void handleAddClient() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username and Email cannot be empty.");
            return;
        }

        Client newClient = new Client(0, name, email, "CLIENT");

        clientService.registerClient(newClient);
        refreshTable();
        nameField.clear();
        emailField.clear();
    }

    private void handleUpdateClient() {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a client to update.");
            return;
        }

        String updatedName = nameField.getText().trim();
        String updatedEmail = emailField.getText().trim();

        if (updatedName.isEmpty() || updatedEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username and Email cannot be empty.");
            return;
        }

        selectedClient.setUsername(updatedName);
        selectedClient.setEmail(updatedEmail);
        clientService.updateClient(selectedClient);
        refreshTable();
        nameField.clear();
        emailField.clear();
    }

    private void handleDeleteClient() {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a client to delete.");
            return;
        }

        clientService.deleteClient(selectedClient.getId());
        refreshTable();
        nameField.clear();
        emailField.clear();
    }


    /**
     * Utility method to display alerts.
     *
     * @param type    the type of alert
     * @param title   the alert title
     * @param message the alert message
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

