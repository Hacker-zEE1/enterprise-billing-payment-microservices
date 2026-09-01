package com.shaqib.billing.customer.controller;

import com.shaqib.billing.customer.dto.CreateCustomerRequest;
import com.shaqib.billing.customer.dto.CustomerResponse;
import com.shaqib.billing.customer.dto.UpdateCustomerRequest;
import com.shaqib.billing.customer.entity.Customer;
import com.shaqib.billing.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService
    ) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request
    ) {

        Customer customer =
                customerService.createCustomer(
                        request.firstName(),
                        request.lastName(),
                        request.email(),
                        request.phoneNumber()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(customer));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(
            @PathVariable UUID customerId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        customerService.getCustomerById(customerId)
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getCustomers() {

        List<CustomerResponse> customers =
                customerService.getAllCustomers()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable UUID customerId,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {

        Customer customer =
                customerService.updateCustomer(
                        customerId,
                        request.firstName(),
                        request.lastName(),
                        request.phoneNumber()
                );

        return ResponseEntity.ok(toResponse(customer));
    }

    @PatchMapping("/{customerId}/deactivate")
    public ResponseEntity<CustomerResponse> deactivateCustomer(
            @PathVariable UUID customerId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        customerService.deactivateCustomer(customerId)
                )
        );
    }

    @PatchMapping("/{customerId}/activate")
    public ResponseEntity<CustomerResponse> activateCustomer(
            @PathVariable UUID customerId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        customerService.activateCustomer(customerId)
                )
        );
    }

    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}