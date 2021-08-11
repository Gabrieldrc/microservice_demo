package com.gdrc.microservice_arq.store.shopping.persistence.repository;

import com.gdrc.microservice_arq.store.shopping.persistence.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    public List<Invoice> findByCustomerId(Long customerId);
    public Invoice findByInvoiceNumber(String numberInvoice);
}
