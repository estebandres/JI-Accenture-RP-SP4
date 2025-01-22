package com.mindhub.rp_sp1.users.exceptions;

public class MissingArgsForUserUpdateException extends Exception{
    public static final String MESSAGE = "STEVE EXCEPTION: Field: %s is missing.";
    public MissingArgsForUserUpdateException(String fieldName) {
        super(String.format(MESSAGE, fieldName));
    }
}
