package com.gdrc.microservice_arq.store.shopping.domain.client;

import com.gdrc.microservice_arq.store.shopping.domain.model.Customer;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service"
)
public interface CustomerClient {
    @GetMapping(value = "/customers/{id}")
    ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id);
}
