package com.shaqib.billing.account.exception;

public class InactiveCustomerException extends RuntimeException {

    public InactiveCustomerException(String message) {
        super(message);
    }
}