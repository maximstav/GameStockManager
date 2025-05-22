package org.example.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.example.dataAccessLayer.BillDAO;
import org.example.model.Bill;

public class LogController {

    @FXML
    private TableView<Bill> logTable;

    private final BillDAO billDAO = new BillDAO();

    @FXML
    public void initialize() {
        TableView<Bill> tableView = billDAO.getTableView();
        logTable.getColumns().addAll(tableView.getColumns());
        logTable.setItems(tableView.getItems());
    }
}
