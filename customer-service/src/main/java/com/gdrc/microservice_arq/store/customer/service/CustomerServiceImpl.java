package com.gdrc.microservice_arq.store.customer.service;

import com.gdrc.microservice_arq.store.customer.repository.CustomerRepository;
import com.gdrc.microservice_arq.store.customer.repository.entity.Customer;
import com.gdrc.microservice_arq.store.customer.repository.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Override
    public List<Customer> findCustomerAll() {
        return repository.findAll();
    }

    @Override
    public List<Customer> findCustomerByRegion(Region region) {
        return repository.findByRegion(region);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Optional<Customer> result = repository.findByIdNumber(customer.getIdNumber());
        if (result.isPresent()) {
            return result.get();
        }
        customer.setState("CREATED");
        Customer customerDB = repository.save(customer);
        return customerDB;
    }

    @Override
    public Optional<Customer> updateCustomer(Customer customer) {
        Optional<Customer> result = getCustomer(customer.getId());
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Customer customerDB = result.get();
        customerDB.setFirstName(customer.getFirstName());
        customerDB.setLastName(customer.getLastName());
        customerDB.setEmail(customer.getEmail());
        customerDB.setPhotoUrl(customer.getPhotoUrl());
        return Optional.of(repository.save(customerDB));
    }

    @Override
    public Optional<Customer> deleteCustomer(Customer customer) {
        Optional<Customer> result = getCustomer(customer.getId());
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Customer customerDB = result.get();
        customerDB.setState("DELETED");
        return Optional.of(repository.save(customerDB));
    }

    @Override
    public Optional<Customer> getCustomer(Long id) {
        return repository.findById(id);
    }
}
