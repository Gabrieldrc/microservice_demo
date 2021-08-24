package com.gdrc.microservice_arq.store.product.exception;

public class NegativeStockQuantityException extends Exception {

    public NegativeStockQuantityException(Double maximumQuantity) {
        super("Quantity is not permitted. Should be less than or equal to " + maximumQuantity);
    }

    public NegativeStockQuantityException() {
        super("Quantity is not permitted");
    }
}
