package com.gdrc.microservice_arq.store.product.service;

import com.gdrc.microservice_arq.store.product.entity.Category;
import com.gdrc.microservice_arq.store.product.entity.Product;
import com.gdrc.microservice_arq.store.product.entity.ProductStatus;
import com.gdrc.microservice_arq.store.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;


    @Override
    public List<Product> listAllProduct() {
        return repository.findAll();
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return repository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus(ProductStatus.CREATED.toString());
        product.setCreateAt(new Date());
        return repository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Optional<Product> result = this.getProduct(product.getId());
        if (result.isEmpty()) {
            return null;
        }
        Product productDb = result.get();
        productDb.setName(product.getName());
        productDb.setDescription(product.getDescription());
        productDb.setCategory(product.getCategory());
        productDb.setPrice(product.getPrice());
        return repository.save(productDb);
    }

    @Override
    public Product deleteProduct(Long id) {
        Optional<Product> result = this.getProduct(id);
        if (result.isEmpty()) {
            return null;
        }
        Product productDb = result.get();
        productDb.setStatus(ProductStatus.DELETED.toString());
        return repository.save(productDb);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return repository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) throws Exception {
        Optional<Product> result = this.getProduct(id);
        if (result.isEmpty()) {
            return null;
        }
        Product productDb = result.get();
        double newStock = productDb.getStock() + quantity;
        if (newStock < 0) {
            throw new Exception("Quantity is not permitted. Should be less than or equal to " + productDb.getStock());
        }
        productDb.setStock(newStock);
        return repository.save(productDb);
    }
}
