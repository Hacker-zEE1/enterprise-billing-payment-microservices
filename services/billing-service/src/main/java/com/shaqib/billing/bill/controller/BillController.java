package com.shaqib.billing.bill.controller;

import com.shaqib.billing.bill.dto.BillResponse;
import com.shaqib.billing.bill.dto.CreateBillRequest;
import com.shaqib.billing.bill.entity.Bill;
import com.shaqib.billing.bill.service.BillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/accounts/{accountId}/bills")
    public ResponseEntity<BillResponse> createBill(
            @PathVariable UUID accountId,
            @Valid @RequestBody CreateBillRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        toResponse(
                                billService.createBill(accountId, request)
                        )
                );
    }

    @GetMapping("/accounts/{accountId}/bills")
    public ResponseEntity<List<BillResponse>> getBillsByAccount(
            @PathVariable UUID accountId
    ) {

        return ResponseEntity.ok(
                billService.getBillsByAccount(accountId)
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @GetMapping("/bills/{billId}")
    public ResponseEntity<BillResponse> getBill(
            @PathVariable UUID billId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        billService.getBillById(billId)
                )
        );
    }

    @PatchMapping("/bills/{billId}/issue")
    public ResponseEntity<BillResponse> issueBill(
            @PathVariable UUID billId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        billService.issueBill(billId)
                )
        );
    }

    @PatchMapping("/bills/{billId}/cancel")
    public ResponseEntity<BillResponse> cancelBill(
            @PathVariable UUID billId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        billService.cancelBill(billId)
                )
        );
    }

    @PatchMapping("/bills/{billId}/paid")
    public ResponseEntity<BillResponse> markPaid(
            @PathVariable UUID billId
    ) {

        return ResponseEntity.ok(
                toResponse(
                        billService.markBillPaid(billId)
                )
        );
    }

    private BillResponse toResponse(Bill bill) {

        return new BillResponse(
                bill.getBillId(),
                bill.getAccountId(),
                bill.getBillNumber(),
                bill.getBillingPeriodStart(),
                bill.getBillingPeriodEnd(),
                bill.getDueDate(),
                bill.getTotalAmount(),
                bill.getStatus(),
                bill.getCreatedAt(),
                bill.getUpdatedAt()
        );
    }
}