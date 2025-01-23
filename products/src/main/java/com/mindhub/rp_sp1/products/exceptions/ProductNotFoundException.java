package com.mindhub.rp_sp1.products.exceptions;

public class ProductNotFoundException extends Exception{
    public static final String MESSAGE = "STEVE EXCEPTION: Product with id: %d was not found.";
    public ProductNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
