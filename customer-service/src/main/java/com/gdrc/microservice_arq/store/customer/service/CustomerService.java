package com.gdrc.microservice_arq.store.customer.service;

import com.gdrc.microservice_arq.store.customer.repository.entity.Customer;
import com.gdrc.microservice_arq.store.customer.repository.entity.Region;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    public List<Customer> findCustomerAll();
    public List<Customer> findCustomerByRegion(Region region);
    public Customer createCustomer(Customer customer);
    public Optional<Customer> updateCustomer(Customer customer);
    public Optional<Customer> deleteCustomer(Customer customer);
    public Optional<Customer> getCustomer(Long id);
}
