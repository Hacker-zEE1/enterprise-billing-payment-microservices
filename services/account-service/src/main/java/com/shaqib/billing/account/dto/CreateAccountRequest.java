package com.shaqib.billing.account.dto;

import com.shaqib.billing.account.entity.AccountType;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(

        @NotNull
        AccountType accountType
) {
}