package com.gdrc.microservice_arq.store.shopping.service;

import com.gdrc.microservice_arq.store.shopping.client.CustomerClient;
import com.gdrc.microservice_arq.store.shopping.client.ProductClient;
import com.gdrc.microservice_arq.store.shopping.model.Customer;
import com.gdrc.microservice_arq.store.shopping.model.Product;
import com.gdrc.microservice_arq.store.shopping.persistence.entity.Invoice;
import com.gdrc.microservice_arq.store.shopping.persistence.entity.InvoiceItem;
import com.gdrc.microservice_arq.store.shopping.persistence.repository.InvoiceItemsRepository;
import com.gdrc.microservice_arq.store.shopping.persistence.repository.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService{

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceItemsRepository invoiceItemsRepository;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private ProductClient productClient;

    @Override
    public List<Invoice> findInvoiceAll() {
        List<Invoice> invoiceList = invoiceRepository.findAll();
        invoiceList = invoiceList.stream().map(invoice -> {
            return this.addProductAndCustomer(invoice);
        }).collect(Collectors.toList());
        return invoiceList;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber());
        if (invoiceDB != null) {
            return invoiceDB;
        }
        invoice.setState("CREATED");
        invoiceDB = invoiceRepository.save(invoice);
        invoiceDB.getItems().forEach(invoiceItem -> {
            productClient.updateStockProduct(invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
        });
        return invoiceDB;
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null) {
            return null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setInvoiceNumber(invoice.getInvoiceNumber());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null) {
            return null;
        }
        invoiceDB.setState("DELETED");
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice getInvoice(Long id) {

        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice != null) {
            return this.addProductAndCustomer(invoice);
        }
        return null;
    }

    private Invoice addProductAndCustomer(Invoice invoice) {
        Customer customer = customerClient.getCustomer(invoice.getCustomerId()).getBody();
        invoice.setCustomer(customer);
        List<InvoiceItem> itemList = invoice.getItems().stream().map(invoiceItem -> {
            Product product = productClient.getProduct(invoiceItem.getProductId()).getBody();
            invoiceItem.setProduct(product);
            return invoiceItem;
        }).collect(Collectors.toList());

        invoice.setItems(itemList);
        return invoice;
    }
}
