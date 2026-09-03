package com.shaqib.billing.bill.service;

import com.shaqib.billing.bill.client.AccountLookupResponse;
import com.shaqib.billing.bill.client.AccountServiceClient;
import com.shaqib.billing.bill.dto.CreateBillRequest;
import com.shaqib.billing.bill.entity.Bill;
import com.shaqib.billing.bill.entity.BillStatus;
import com.shaqib.billing.bill.exception.BillNotFoundException;
import com.shaqib.billing.bill.exception.InactiveAccountException;
import com.shaqib.billing.bill.repository.BillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final AccountServiceClient accountServiceClient;

    public BillService(
            BillRepository billRepository,
            AccountServiceClient accountServiceClient
    ) {
        this.billRepository = billRepository;
        this.accountServiceClient = accountServiceClient;
    }

    @Transactional
    public Bill createBill(
            UUID accountId,
            CreateBillRequest request
    ) {

        AccountLookupResponse account =
                accountServiceClient.getAccount(accountId);

        if (!"ACTIVE".equalsIgnoreCase(account.status())) {
            throw new InactiveAccountException(
                    "Cannot create bill for inactive account"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        Bill bill = new Bill(
                UUID.randomUUID(),
                accountId,
                generateBillNumber(),
                request.billingPeriodStart(),
                request.billingPeriodEnd(),
                request.dueDate(),
                request.totalAmount(),
                BillStatus.DRAFT,
                now,
                now
        );

        return billRepository.save(bill);
    }

    @Transactional(readOnly = true)
    public Bill getBillById(UUID billId) {

        return billRepository.findById(billId)
                .orElseThrow(() ->
                        new BillNotFoundException(
                                "Bill not found with id: " + billId
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Bill> getBillsByAccount(UUID accountId) {

        accountServiceClient.getAccount(accountId);

        return billRepository.findByAccountId(accountId);
    }

    @Transactional
    public Bill issueBill(UUID billId) {

        Bill bill = getBillById(billId);

        bill.issue(LocalDateTime.now());

        return billRepository.save(bill);
    }

    @Transactional
    public Bill cancelBill(UUID billId) {

        Bill bill = getBillById(billId);

        bill.cancel(LocalDateTime.now());

        return billRepository.save(bill);
    }

    @Transactional
    public Bill markBillPaid(UUID billId) {

        Bill bill = getBillById(billId);

        bill.markPaid(LocalDateTime.now());

        return billRepository.save(bill);
    }

    private String generateBillNumber() {

        return "BILL-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }
}