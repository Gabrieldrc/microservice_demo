package com.gdrc.microservice_arq.store.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdrc.microservice_arq.store.product.entity.Category;
import com.gdrc.microservice_arq.store.product.entity.Product;
import com.gdrc.microservice_arq.store.product.error.ErrorMessage;
import com.gdrc.microservice_arq.store.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listProduct(
            @RequestParam(name = "categoryId", required = false) Long categoryId
    ) {
        List<Product> products = new ArrayList<>();
        if (categoryId == null) {
            products = productService.listAllProduct();
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } else {
            products = productService.findByCategory(Category.builder().id(categoryId).build());
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        Optional<Product> result = productService.getProduct(id);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Product productCreated = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable(name = "id") Long id, @RequestBody Product product) {
        Product productDB = productService.updateProduct(product);
        if (productDB == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDB);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id) {
        Product productDelete = productService.deleteProduct(id);
        if (productDelete == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDelete);
    }

    @GetMapping(value = "/{id}/stock")
    public ResponseEntity<Product> updateStockProduct(@PathVariable(name = "id") Long id, @RequestParam(name = "quantity") Double quantity) {
        Product product = productService.updateStock(id, quantity);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    private String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
