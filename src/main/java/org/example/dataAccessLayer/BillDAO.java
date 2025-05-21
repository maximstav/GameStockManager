package org.example.dataAccessLayer;

import org.example.model.Bill;

import java.util.logging.Level;

/**
 * BillDAO is a specialized DAO class for the immutable Bill model.
 * It overrides update and delete methods to disable modification of the Log table.
 */
public class BillDAO extends AbstractDAO<Bill> {
    /**
     * Returns the name of the Log table.
     *
     * @return "Log"
     */
    @Override
    protected String getTableName() {
        return "Log";
    }

    /**
     * Not supported for Bill records. Logs a warning and throws an exception.
     *
     * @param t the Bill object
     * @return nothing
     * @throws UnsupportedOperationException always
     */
    @Override
    public Bill update(Bill t) {
        LOGGER.log(Level.WARNING, "Update not supported for Bill (Log table)");
        throw new UnsupportedOperationException("Update not supported for Bill (Log table)");
    }

    /**
     * Not supported for Bill records. Logs a warning and throws an exception.
     *
     * @param id the ID of the bill to delete
     * @return nothing
     * @throws UnsupportedOperationException always
     */
    @Override
    public boolean delete(int id) {
        LOGGER.log(Level.WARNING, "Delete not supported for Bill (Log table)");
        throw new UnsupportedOperationException("Delete not supported for Bill (Log table)");
    }
}
