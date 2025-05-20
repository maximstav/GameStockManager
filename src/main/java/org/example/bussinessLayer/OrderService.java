package org.example.bussinessLayer;

import org.example.dataAccessLayer.BillDAO;
import org.example.dataAccessLayer.OrderDAO;
import org.example.dataAccessLayer.ProductDAO;
import org.example.model.Bill;
import org.example.model.Order;
import org.example.model.Product;

import java.sql.Timestamp;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final BillDAO billDAO = new BillDAO(); // handles Bills - Logs in the DB

    public boolean placeOrder(int clientId, int productId, int quantity) {
        Product product = productDAO.findById(productId);
        if (product.getQuantity() < quantity) {
            System.out.println("Not enough quantity of " + product.getQuantity() + "(id: " + product.getId() + ")");
            return false;
        }

        Order order = new Order(0, clientId, productId, quantity, new Timestamp(System.currentTimeMillis()));
        orderDAO.insert(order);

        // Update stock
        product.setQuantity(product.getQuantity() - quantity);
        productDAO.update(product);

        // Create and insert bill
        String billText = "Client ID: " + clientId + "\nProduct: " + quantity + "x " + product.getName() +
                "\nTotal: $" + (quantity * product.getPrice());
        Bill bill = new Bill(0, order.getId(), billText, order.getTimestamp());
        billDAO.insert(bill);

        return true;
    }
}

