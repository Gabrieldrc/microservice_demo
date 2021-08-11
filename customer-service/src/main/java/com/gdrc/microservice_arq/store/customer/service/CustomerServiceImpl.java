package com.gdrc.microservice_arq.store.customer.service;

import com.gdrc.microservice_arq.store.customer.repository.CustomerRepository;
import com.gdrc.microservice_arq.store.customer.repository.entity.Customer;
import com.gdrc.microservice_arq.store.customer.repository.entity.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Customer customerDB = repository.findByIdNumber(customer.getIdNumber());
        if (customerDB != null) {
            return customerDB;
        }
        customer.setState("CREATED");
        customerDB = repository.save(customer);
        return customerDB;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer customerDB = getCustomer(customer.getId());
        if (customerDB == null) {
            return null;
        }
        customerDB.setFirstName(customer.getFirstName());
        customerDB.setLastName(customer.getLastName());
        customerDB.setEmail(customer.getEmail());
        customerDB.setPhotoUrl(customer.getPhotoUrl());
        return repository.save(customerDB);
    }

    @Override
    public Customer deleteCustomer(Customer customer) {
        Customer customerDB = getCustomer(customer.getId());
        if (customerDB == null) {
            return null;
        }
        customer.setState("DELETED");
        return repository.save(customer);
    }

    @Override
    public Customer getCustomer(Long id) {
        return repository.findById(id).orElse(null);
    }
}
