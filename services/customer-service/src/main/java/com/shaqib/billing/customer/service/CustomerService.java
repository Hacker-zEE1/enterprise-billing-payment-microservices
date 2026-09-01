package com.shaqib.billing.customer.service;

import com.shaqib.billing.customer.entity.Customer;
import com.shaqib.billing.customer.entity.CustomerStatus;
import com.shaqib.billing.customer.exception.CustomerNotFoundException;
import com.shaqib.billing.customer.exception.DuplicateCustomerEmailException;
import com.shaqib.billing.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(
            CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer createCustomer(
            String firstName,
            String lastName,
            String email,
            String phoneNumber
    ) {

        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateCustomerEmailException(
                    "Customer with this email already exists"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        Customer customer =
                new Customer(
                        UUID.randomUUID(),
                        firstName,
                        lastName,
                        email,
                        phoneNumber,
                        CustomerStatus.ACTIVE,
                        now,
                        now
                );

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(UUID customerId) {

        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: "
                                        + customerId
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer updateCustomer(
            UUID customerId,
            String firstName,
            String lastName,
            String phoneNumber
    ) {

        Customer customer =
                getCustomerById(customerId);

        customer.updateDetails(
                firstName,
                lastName,
                phoneNumber,
                LocalDateTime.now()
        );

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer deactivateCustomer(UUID customerId) {

        Customer customer =
                getCustomerById(customerId);

        customer.deactivate(LocalDateTime.now());

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer activateCustomer(UUID customerId) {

        Customer customer =
                getCustomerById(customerId);

        customer.activate(LocalDateTime.now());

        return customerRepository.save(customer);
    }
}