package com.mindhub.rp_sp1.orders.exceptions;

public class StockInsufficientException extends Exception {
    public StockInsufficientException() {
        super("STEVE EXCEPTION: Stock is insufficient.");
    }
}
