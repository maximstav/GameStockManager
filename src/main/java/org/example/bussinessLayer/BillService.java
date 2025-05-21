package org.example.bussinessLayer;

import org.example.dataAccessLayer.BillDAO;
import org.example.model.Bill;
import java.util.List;

public class BillService {
    private final BillDAO billDAO = new BillDAO();
    public List<Bill> getAllBills() {
        return billDAO.findAll();
    }
}
