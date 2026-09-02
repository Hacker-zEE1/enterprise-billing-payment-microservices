package com.shaqib.billing.account.controller;

import com.shaqib.billing.account.dto.AccountResponse;
import com.shaqib.billing.account.dto.CreateAccountRequest;
import com.shaqib.billing.account.entity.Account;
import com.shaqib.billing.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService accountService;

    public AccountController(
            AccountService accountService
    ) {
        this.accountService = accountService;
    }

    @PostMapping("/customers/{customerId}/accounts")
    public ResponseEntity<AccountResponse> createAccount(
            @PathVariable UUID customerId,
            @Valid @RequestBody CreateAccountRequest request
    ) {

        Account account =
                accountService.createAccount(
                        customerId,
                        request.accountType()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(account));
    }

    @GetMapping("/customers/{customerId}/accounts")
    public ResponseEntity<List<AccountResponse>> getCustomerAccounts(
            @PathVariable UUID customerId
    ) {

        List<AccountResponse> accounts =
                accountService.getAccountsByCustomer(customerId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable UUID accountId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        accountService.getAccountById(accountId)
                )
        );
    }

    @PatchMapping("/accounts/{accountId}/deactivate")
    public ResponseEntity<AccountResponse> deactivateAccount(
            @PathVariable UUID accountId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        accountService.deactivateAccount(accountId)
                )
        );
    }

    @PatchMapping("/accounts/{accountId}/activate")
    public ResponseEntity<AccountResponse> activateAccount(
            @PathVariable UUID accountId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        accountService.activateAccount(accountId)
                )
        );
    }

    private AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getAccountId(),
                account.getCustomerId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}