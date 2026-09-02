package com.shaqib.billing.account.client;

import com.shaqib.billing.account.exception.CustomerNotAvailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class CustomerServiceClient {

    private final RestClient restClient;

    public CustomerServiceClient(
            @Value("${clients.customer-service.base-url}")
            String customerServiceUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(customerServiceUrl)
                .build();
    }

    public CustomerLookupResponse getCustomer(UUID customerId) {

        try {

            return restClient.get()
                    .uri("/api/v1/customers/{customerId}", customerId)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            (request, response) -> {
                                throw new CustomerNotAvailableException(
                                        "Customer is not available: " + customerId
                                );
                            }
                    )
                    .body(CustomerLookupResponse.class);

        } catch (CustomerNotAvailableException exception) {
            throw exception;

        } catch (RestClientException exception) {
            throw new CustomerNotAvailableException(
                    "Customer service is currently unavailable"
            );
        }
    }
}