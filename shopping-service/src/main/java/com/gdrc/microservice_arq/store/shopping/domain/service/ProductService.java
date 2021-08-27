package com.gdrc.microservice_arq.store.shopping.domain.service;

import com.gdrc.microservice_arq.store.shopping.domain.client.ProductClient;
import com.gdrc.microservice_arq.store.shopping.domain.model.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final static String PRODUCT_SERVICE = "productService";

    @Autowired
    private ProductClient productClient;

    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "getFallbackProduct")
    public Optional<Product> getProductById(Long id) {
        ResponseEntity<Product> response = productClient.getProduct(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            Product product = response.getBody();
            return Optional.of(product);
        }
        return Optional.empty();
    }

    public Optional<Product> getFallbackProduct(Long id) {
        Product product = Product.builder()
                .name("none")
                .description("none")
                .price((double) 0)
                .build();
        return Optional.of(product);
    }
}
