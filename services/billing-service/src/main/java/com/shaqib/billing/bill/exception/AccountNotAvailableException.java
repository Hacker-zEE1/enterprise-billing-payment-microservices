package com.shaqib.billing.bill.exception;

public class AccountNotAvailableException extends RuntimeException {

    public AccountNotAvailableException(String message) {
        super(message);
    }
}