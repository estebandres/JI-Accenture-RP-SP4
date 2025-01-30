package com.mindhub.rp_sp1.users.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String email) {
        super(String.format("STEVE EXCEPTION: User with email: %s already exists.", email));
    }
}
