package org.example.bussinessLayer;

import org.example.dataAccessLayer.ProductDAO;
import org.example.model.Product;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Product updateProduct(Product product) {
        return productDAO.update(product);
    }

    public Product addProduct(Product product) {
        return productDAO.insert(product);
    }

    public Product getProductById(int id) {
        return productDAO.findById(id);
    }

    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }
}