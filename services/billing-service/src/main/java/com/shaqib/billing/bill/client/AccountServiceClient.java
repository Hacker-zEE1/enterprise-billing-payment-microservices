package com.shaqib.billing.bill.client;

import com.shaqib.billing.bill.exception.AccountNotAvailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class AccountServiceClient {

    private final RestClient restClient;

    public AccountServiceClient(
            @Value("${clients.account-service.base-url}")
            String accountServiceUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(accountServiceUrl)
                .build();
    }

    public AccountLookupResponse getAccount(UUID accountId) {

        try {

            return restClient.get()
                    .uri("/api/v1/accounts/{accountId}", accountId)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            (request, response) -> {
                                throw new AccountNotAvailableException(
                                        "Account is not available: " + accountId
                                );
                            }
                    )
                    .body(AccountLookupResponse.class);

        } catch (AccountNotAvailableException exception) {
            throw exception;

        } catch (RestClientException exception) {
            throw new AccountNotAvailableException(
                    "Account service is currently unavailable"
            );
        }
    }
}