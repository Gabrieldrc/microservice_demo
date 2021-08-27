package com.gdrc.microservice_arq.store.shopping.api.service;

import com.gdrc.microservice_arq.store.shopping.domain.model.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> getCustomerById(Long id);
}
