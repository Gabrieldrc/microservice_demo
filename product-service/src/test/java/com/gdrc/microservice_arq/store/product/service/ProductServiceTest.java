package com.gdrc.microservice_arq.store.product.service;

import com.gdrc.microservice_arq.store.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;

@Service
final class ProductServiceTest {

    @MockBean
    private ProductRepository repository;

    @Autowired
    private ProductService undertest;

//    @Test
//    public void
}