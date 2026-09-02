package com.shaqib.billing.account.service;

import com.shaqib.billing.account.client.CustomerLookupResponse;
import com.shaqib.billing.account.client.CustomerServiceClient;
import com.shaqib.billing.account.entity.Account;
import com.shaqib.billing.account.entity.AccountStatus;
import com.shaqib.billing.account.entity.AccountType;
import com.shaqib.billing.account.exception.AccountNotFoundException;
import com.shaqib.billing.account.exception.InactiveCustomerException;
import com.shaqib.billing.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerServiceClient customerServiceClient;

    public AccountService(
            AccountRepository accountRepository,
            CustomerServiceClient customerServiceClient
    ) {
        this.accountRepository = accountRepository;
        this.customerServiceClient = customerServiceClient;
    }

    @Transactional
    public Account createAccount(
            UUID customerId,
            AccountType accountType
    ) {

        CustomerLookupResponse customer =
                customerServiceClient.getCustomer(customerId);

        if (!"ACTIVE".equalsIgnoreCase(customer.status())) {
            throw new InactiveCustomerException(
                    "Cannot create account for inactive customer"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        Account account =
                new Account(
                        UUID.randomUUID(),
                        customerId,
                        generateAccountNumber(),
                        accountType,
                        AccountStatus.ACTIVE,
                        now,
                        now
                );

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccountById(UUID accountId) {

        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with id: " + accountId
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsByCustomer(UUID customerId) {

        customerServiceClient.getCustomer(customerId);

        return accountRepository.findByCustomerId(customerId);
    }

    @Transactional
    public Account deactivateAccount(UUID accountId) {

        Account account = getAccountById(accountId);

        account.deactivate(LocalDateTime.now());

        return accountRepository.save(account);
    }

    @Transactional
    public Account activateAccount(UUID accountId) {

        Account account = getAccountById(accountId);

        account.activate(LocalDateTime.now());

        return accountRepository.save(account);
    }

    private String generateAccountNumber() {

        return "ACC-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }
}