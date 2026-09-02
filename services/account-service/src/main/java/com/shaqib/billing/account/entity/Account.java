package com.shaqib.billing.account.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "account_number", nullable = false, unique = true, length = 50)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 30)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Account() {
    }

    public Account(
            UUID accountId,
            UUID customerId,
            String accountNumber,
            AccountType accountType,
            AccountStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void deactivate(LocalDateTime updatedAt) {
        this.status = AccountStatus.INACTIVE;
        this.updatedAt = updatedAt;
    }

    public void activate(LocalDateTime updatedAt) {
        this.status = AccountStatus.ACTIVE;
        this.updatedAt = updatedAt;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}