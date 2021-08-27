package com.gdrc.microservice_arq.store.customer.repository;

import com.gdrc.microservice_arq.store.customer.repository.entity.Customer;
import com.gdrc.microservice_arq.store.customer.repository.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Optional<Customer> findById(Long id);
    public Optional<Customer> findByIdNumber(String idNumber);
    public List<Customer> findByLastName(String lastName);
    public List<Customer> findByRegion(Region region);

}
