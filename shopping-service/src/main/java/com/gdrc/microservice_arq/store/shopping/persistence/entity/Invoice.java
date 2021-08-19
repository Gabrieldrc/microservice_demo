package com.gdrc.microservice_arq.store.shopping.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gdrc.microservice_arq.store.shopping.model.Customer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    private String description;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @Valid
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "invoice_id", nullable = false)
    private List<InvoiceItem> items;

    private String state;

    @Transient
    private Customer customer;

    @PrePersist
    public void prePersist() {
        this.createAt = new Date();
    }
}
