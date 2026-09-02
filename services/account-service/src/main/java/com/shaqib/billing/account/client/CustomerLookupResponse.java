package com.shaqib.billing.account.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerLookupResponse(
        UUID customerId,
        String status
) {
}