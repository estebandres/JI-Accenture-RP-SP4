package com.mindhub.rp_sp1.users.exceptions;

public class UserNotFoundException extends Exception{
    public static final String MESSAGE = "STEVE EXCEPTION: User with id: %d was not found.";
    public UserNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
