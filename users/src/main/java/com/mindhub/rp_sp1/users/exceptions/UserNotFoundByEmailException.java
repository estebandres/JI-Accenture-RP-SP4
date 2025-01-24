package com.mindhub.rp_sp1.users.exceptions;

public class UserNotFoundByEmailException extends Exception {
    public UserNotFoundByEmailException(String email) {
        super(String.format("STEVE EXCEPTION: User with email: %s was not found.", email));
    }
}
