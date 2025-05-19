package org.example.bussinessLayer;

import org.example.dataAccessLayer.ProductDAO;
import org.example.model.Product;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public void updateProduct(Product product) {
        productDAO.update(product);
    }

    public void addProduct(Product product) {
        productDAO.insert(product);
    }

    public Product getProductById(int id) {
        return productDAO.findById(id);
    }
}