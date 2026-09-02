package com.shaqib.billing.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAccountNotFound(
            AccountNotFoundException exception
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(CustomerNotAvailableException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerNotAvailable(
            CustomerNotAvailableException exception
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(InactiveCustomerException.class)
    public ResponseEntity<ApiErrorResponse> handleInactiveCustomer(
            InactiveCustomerException exception
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception
    ) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error ->
                        error.getField() + ": " +
                                error.getDefaultMessage()
                )
                .orElse("Invalid request");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message
    ) {

        ApiErrorResponse response =
                new ApiErrorResponse(
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}