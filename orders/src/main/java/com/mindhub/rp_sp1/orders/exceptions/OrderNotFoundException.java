package com.mindhub.rp_sp1.orders.exceptions;

public class OrderNotFoundException extends Exception{
    public static final String MESSAGE = "STEVE EXCEPTION: Order with id: %d was not found.";

    public OrderNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }

}
