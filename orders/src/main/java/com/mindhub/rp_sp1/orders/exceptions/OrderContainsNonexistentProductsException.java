package com.mindhub.rp_sp1.orders.exceptions;

public class OrderContainsNonexistentProductsException extends Exception{
    public OrderContainsNonexistentProductsException() {
        super("STEVE EXCEPTION: Order contains nonexistent products.");
    }
}
