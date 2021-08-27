package com.gdrc.microservice_arq.store.shopping.domain.service;

import com.gdrc.microservice_arq.store.shopping.domain.client.CustomerClient;
import com.gdrc.microservice_arq.store.shopping.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerClient customerClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    public Optional<Customer> getCustomerById(Long id) {
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("customer-service");
        Supplier<ResponseEntity<Customer>> responseEntitySupplier = () -> customerClient.getCustomer(id);
        ResponseEntity<Customer> response = circuitBreaker.run(responseEntitySupplier, throwable -> getFallbackCustomer());
        if (response.getStatusCode().is2xxSuccessful()) {
            Customer customer = response.getBody();
            return Optional.of(customer);
        }
        return Optional.empty();
    }

    private ResponseEntity<Customer> requestCustomerByIdToCustomerClient(Long id) {
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("customer-service");
        Supplier<ResponseEntity<Customer>> responseEntitySupplier = () -> customerClient.getCustomer(id);
        return circuitBreaker.run(responseEntitySupplier, throwable -> getFallbackCustomer());
    }

    public ResponseEntity<Customer> getFallbackCustomer(){
        Customer fallbackCustomer = Customer.builder()
                .email("none")
                .firstName("none")
                .lastName("none")
                .photoUrl("none")
                .build();
        return ResponseEntity.ok(fallbackCustomer);
    }
}
