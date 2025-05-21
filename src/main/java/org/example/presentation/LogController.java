package org.example.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.bussinessLayer.BillService;
import org.example.model.Bill;

public class LogController {

    @FXML
    private TableView<Bill> logTable;

    @FXML
    private TableColumn<Bill, Integer> idColumn;

    @FXML
    private TableColumn<Bill, Integer> orderIdColumn;

    @FXML
    private TableColumn<Bill, String> billTextColumn;

    @FXML
    private TableColumn<Bill, java.sql.Timestamp> createdAtColumn;

    private final BillService billService = new BillService();

    @FXML
    public void initialize() {
        // Set up columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        billTextColumn.setCellValueFactory(new PropertyValueFactory<>("billText"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Load data
        ObservableList<Bill> bills = FXCollections.observableArrayList(billService.getAllBills());
        logTable.setItems(bills);
    }
}
