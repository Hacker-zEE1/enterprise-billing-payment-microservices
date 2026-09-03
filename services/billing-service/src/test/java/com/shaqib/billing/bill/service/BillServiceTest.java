package com.shaqib.billing.bill.service;

import com.shaqib.billing.bill.client.AccountLookupResponse;
import com.shaqib.billing.bill.client.AccountServiceClient;
import com.shaqib.billing.bill.dto.CreateBillRequest;
import com.shaqib.billing.bill.exception.AccountNotAvailableException;
import com.shaqib.billing.bill.exception.InactiveAccountException;
import com.shaqib.billing.bill.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BillServiceTest {

    private BillRepository billRepository;
    private AccountServiceClient accountServiceClient;
    private BillService billService;

    @BeforeEach
    void setUp() {
        billRepository = Mockito.mock(BillRepository.class);
        accountServiceClient = Mockito.mock(AccountServiceClient.class);

        billService = new BillService(
                billRepository,
                accountServiceClient
        );
    }

    @Test
    void shouldRejectBillCreationForInactiveAccount() {

        UUID accountId = UUID.randomUUID();

        when(accountServiceClient.getAccount(accountId))
                .thenReturn(
                        new AccountLookupResponse(
                                accountId,
                                UUID.randomUUID(),
                                "INACTIVE"
                        )
                );

        CreateBillRequest request =
                new CreateBillRequest(
                        LocalDate.of(2026, 9, 1),
                        LocalDate.of(2026, 9, 30),
                        LocalDate.of(2026, 10, 15),
                        new BigDecimal("100.00")
                );

        assertThrows(
                InactiveAccountException.class,
                () -> billService.createBill(accountId, request)
        );

        verify(billRepository, never()).save(any());
    }

    @Test
    void shouldRejectBillCreationWhenAccountServiceIsUnavailable() {

        UUID accountId = UUID.randomUUID();

        when(accountServiceClient.getAccount(accountId))
                .thenThrow(
                        new AccountNotAvailableException(
                                "Account service is currently unavailable"
                        )
                );

        CreateBillRequest request =
                new CreateBillRequest(
                        LocalDate.of(2026, 9, 1),
                        LocalDate.of(2026, 9, 30),
                        LocalDate.of(2026, 10, 15),
                        new BigDecimal("100.00")
                );

        assertThrows(
                AccountNotAvailableException.class,
                () -> billService.createBill(accountId, request)
        );

        verify(billRepository, never()).save(any());
    }
}