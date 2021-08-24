package com.gdrc.microservice_arq.store.shopping.service;

import com.gdrc.microservice_arq.store.shopping.client.CustomerClient;
import com.gdrc.microservice_arq.store.shopping.client.ProductClient;
import com.gdrc.microservice_arq.store.shopping.model.Customer;
import com.gdrc.microservice_arq.store.shopping.model.Product;
import com.gdrc.microservice_arq.store.shopping.persistence.entity.Invoice;
import com.gdrc.microservice_arq.store.shopping.persistence.entity.InvoiceItem;
import com.gdrc.microservice_arq.store.shopping.persistence.entity.InvoiceState;
import com.gdrc.microservice_arq.store.shopping.persistence.repository.InvoiceItemsRepository;
import com.gdrc.microservice_arq.store.shopping.persistence.repository.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return addProductsAndCustomersData(invoiceList);
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Optional<Invoice> result = invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber());
        if (result.isPresent()) {
            return result.get();
        }
        invoice.setState(InvoiceState.CREATED.toString());
        Invoice invoiceDB = invoiceRepository.save(invoice);
        updateStockProducts(invoiceDB);
        return invoiceDB;
    }

    @Override
    public Optional<Invoice> updateInvoice(Invoice invoice) {
        Optional<Invoice> result = getInvoiceById(invoice.getId());
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Invoice invoiceDB = result.get();
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setInvoiceNumber(invoice.getInvoiceNumber());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        return Optional.of(invoiceRepository.save(invoiceDB));
    }

    @Override
    public Optional<Invoice> deleteInvoice(Invoice invoice) {
        Optional<Invoice> result = getInvoiceById(invoice.getId());
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return result.map(invoiceDB -> {
            invoiceDB.setState(InvoiceState.DELETED.toString());
            return invoiceRepository.save(invoiceDB);
        });
    }

    @Override
    public Optional<Invoice> getInvoiceById(Long id) {
        Optional<Invoice> result = invoiceRepository.findById(id);
        if (result.isPresent()) {
            return Optional.of(this.addProductAndCustomer(result.get()));
        }
        return Optional.empty();
    }

    private List<Invoice> addProductsAndCustomersData(List<Invoice> invoiceList) {
        return invoiceList.stream().map(invoice -> {
            return this.addProductAndCustomer(invoice);
        }).collect(Collectors.toList());
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

    private void updateStockProducts(Invoice invoiceDB) {
        invoiceDB.getItems().forEach(invoiceItem -> {
            productClient.updateStockProduct(invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
        });
    }
}
