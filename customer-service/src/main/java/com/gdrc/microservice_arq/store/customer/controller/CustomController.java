package com.gdrc.microservice_arq.store.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdrc.microservice_arq.store.customer.error.ErrorMessage;
import com.gdrc.microservice_arq.store.customer.repository.entity.Customer;
import com.gdrc.microservice_arq.store.customer.repository.entity.Region;
import com.gdrc.microservice_arq.store.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomers(
            @RequestParam(name = "regionId", required = false) Long regionId
    ) {
        List<Customer> customerList = new ArrayList<>();
        if (!isFilterByRegionId(regionId)) {
            customerList = customerService.findCustomerAll();
            if (customerList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        } else {
            Region region = new Region();
            region.setId(regionId);
            customerList = customerService.findCustomerByRegion(region);
            if (customerList.isEmpty()) {
                log.error("Customers with Region id {} not found.", regionId);
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(customerList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(
            @PathVariable("id") Long id
    ) {
        log.info("Fetching Customer with id {}", id);
        Optional<Customer> result = customerService.getCustomer(id);
        if (result.isEmpty()) {
            log.error("Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody Customer customer,
            BindingResult result
    ) {
        log.info("Creating Customer: {}", customer);
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Customer customerDB = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable("id") long id,
            @RequestBody Customer customer
    ) {
        log.info("Updating Customer with id {}", id);
        customer.setId(id);
        Optional<Customer> result = customerService.updateCustomer(customer);
        if (result.isEmpty()) {
            log.error("Unable to update. Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustomer(
            @PathVariable("id") long id
    ) {
        log.info("Fetching & Deleting Customer with id {}", id);
        Optional<Customer> result = customerService.deleteCustomer(
                Customer.builder().id(id).build()
        );
        if (result.isEmpty()) {
            log.error("Unable to delete. Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
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

    private boolean isFilterByRegionId(Long regionId) {
        return regionId != null;
    }

}
