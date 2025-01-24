package com.mindhub.rp_sp1.orders.exceptions;

public class InsufficientStockForOrderCompletionException extends Exception {

    public InsufficientStockForOrderCompletionException() {
        super("STEVE EXCEPTION: Insufficient stock for order completion.");
    }
}
