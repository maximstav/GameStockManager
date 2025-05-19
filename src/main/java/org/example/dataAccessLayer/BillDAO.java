package org.example.dataAccessLayer;

import org.example.model.Bill;

public class BillDAO extends AbstractDAO<Bill> {
    @Override
    protected String getTableName() {
        return "Log";
    }

    @Override
    public Bill update(Bill t) {
        throw new UnsupportedOperationException("Update not supported for Bill (Log table)");
    }
}
