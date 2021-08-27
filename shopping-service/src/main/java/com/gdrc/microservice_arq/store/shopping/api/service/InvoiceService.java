package com.gdrc.microservice_arq.store.shopping.api.service;

import com.gdrc.microservice_arq.store.shopping.persistence.entity.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    public List<Invoice> findInvoiceAll();
    public Invoice createInvoice(Invoice invoice);
    public Optional<Invoice> updateInvoice(Invoice invoice);
    public Optional<Invoice> deleteInvoice(Invoice invoice);
    public Optional<Invoice> getInvoiceById(Long id);
}
