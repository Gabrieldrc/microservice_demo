package com.gdrc.microservice_arq.store.product.repository;

import com.gdrc.microservice_arq.store.product.entity.Category;
import com.gdrc.microservice_arq.store.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCategory(Category category);
}
