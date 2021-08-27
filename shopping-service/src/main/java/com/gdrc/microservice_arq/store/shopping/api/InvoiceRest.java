package com.gdrc.microservice_arq.store.shopping.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdrc.microservice_arq.store.shopping.api.dto.ErrorMessage;
import com.gdrc.microservice_arq.store.shopping.persistence.entity.Invoice;
import com.gdrc.microservice_arq.store.shopping.api.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/invoices")
public class InvoiceRest {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> listAllInvoices() {
        List<Invoice> invoicesList = invoiceService.findInvoiceAll();
        if (invoicesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(invoicesList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Invoice> getInvoice(
            @PathVariable("id") long id
    ) {
        log.info("Fetching Invoice with id {}", id);
        Optional<Invoice> result = invoiceService.getInvoiceById(id);
        if (result.isEmpty()) {
            log.error("Invoice with id {} not found." , id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(
            @Valid @RequestBody Invoice invoice,
            BindingResult result
    ) {
        log.info("Creating Invoice: {}", invoice);
        if (result.hasErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    this.formatMessage(result)
            );
        }
        Invoice invoiceDB = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceDB);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateInvoice(
            @PathVariable("id") long id,
            @RequestBody Invoice invoice
    ) {
        log.info("Updating Invoice with id {}", id);
        invoice.setId(id);
        Optional<Invoice> invoiceDB = invoiceService.updateInvoice(invoice);

        if (invoiceDB.isEmpty()) {
            log.error("Unable to update. Invoice with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoiceDB.get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Invoice> deleteInvoice(
            @PathVariable("id") long id
    ) {
        log.info("Fetching & Deleting Invoice with id {}", id);
        Optional<Invoice> invoiceDB = invoiceService.getInvoiceById(id);
        if (invoiceDB.isEmpty()) {
            log.error("Unable to delete. Invoice with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        invoiceDB = invoiceService.deleteInvoice(invoiceDB.get());
        return ResponseEntity.ok(invoiceDB.get());
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
