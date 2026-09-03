package com.shaqib.billing.bill.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateBillRequest(

        @NotNull
        LocalDate billingPeriodStart,

        @NotNull
        LocalDate billingPeriodEnd,

        @NotNull
        LocalDate dueDate,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal totalAmount
) {
}