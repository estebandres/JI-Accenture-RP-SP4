package com.mindhub.rp_sp1.orders.exceptions;

public class SiteUserNotFoundException extends Exception{
    public static final String MESSAGE = "STEVE EXCEPTION: User with email: %s was not found.";
    public SiteUserNotFoundException(String email) {
        super(String.format(MESSAGE, email));
    }
}
