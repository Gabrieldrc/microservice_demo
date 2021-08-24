package com.gdrc.microservice_arq.store.product.service;

import com.gdrc.microservice_arq.store.product.entity.Category;
import com.gdrc.microservice_arq.store.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public List<Product> listAllProduct();
    public Optional<Product> getProduct(Long id);
    public Product createProduct(Product product);
    public Optional<Product> updateProduct(Product product);
    public Optional<Product> deleteProduct(Long id);
    public List<Product> findByCategory(Category category);
    public Optional<Product> updateStock(Long id, Double quantity) throws Exception;
}
