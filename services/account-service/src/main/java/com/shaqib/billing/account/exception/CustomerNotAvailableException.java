package com.shaqib.billing.account.exception;

public class CustomerNotAvailableException extends RuntimeException {

    public CustomerNotAvailableException(String message) {
        super(message);
    }
}