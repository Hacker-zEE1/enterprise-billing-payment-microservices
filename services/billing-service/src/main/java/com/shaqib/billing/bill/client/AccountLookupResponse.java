package com.shaqib.billing.bill.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountLookupResponse(
        UUID accountId,
        UUID customerId,
        String status
) {
}