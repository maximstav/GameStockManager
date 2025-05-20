package org.example.dataAccessLayer;

import org.example.model.Bill;

import java.util.logging.Level;

public class BillDAO extends AbstractDAO<Bill> {
    @Override
    protected String getTableName() {
        return "Log";
    }

    @Override
    public Bill update(Bill t) {
        LOGGER.log(Level.WARNING, "Update not supported for Bill (Log table)");
        throw new UnsupportedOperationException("Update not supported for Bill (Log table)");
    }

    @Override
    public boolean delete(int id) {
        LOGGER.log(Level.WARNING, "Delete not supported for Bill (Log table)");
        throw new UnsupportedOperationException("Delete not supported for Bill (Log table)");
    }
}
