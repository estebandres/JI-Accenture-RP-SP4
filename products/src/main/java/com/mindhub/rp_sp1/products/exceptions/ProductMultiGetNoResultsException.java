package com.mindhub.rp_sp1.products.exceptions;

public class ProductMultiGetNoResultsException extends Exception{
    public ProductMultiGetNoResultsException() {
        super("STEVE EXCEPTION: No products found.");
    }
}
