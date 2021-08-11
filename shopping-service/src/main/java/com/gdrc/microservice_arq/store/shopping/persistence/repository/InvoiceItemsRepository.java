package com.gdrc.microservice_arq.store.shopping.persistence.repository;

import com.gdrc.microservice_arq.store.shopping.persistence.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemsRepository extends JpaRepository<InvoiceItem, Long> {
}
