package com.alacriti.reference.controller;

import com.alacriti.reference.entity.Transaction;
import com.alacriti.reference.model.input.CreateTransactionInput;
import com.alacriti.reference.model.type.TransactionStatus;
import com.alacriti.reference.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<Transaction> process(
            @RequestBody CreateTransactionInput request,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                service.process(
                        request.getVirtualCardId(),
                        request.getMerchantName(),
                        request.getAmount(),
                        request.getDescription(),
                        email
                )
        );
    }

    @GetMapping
    public ResponseEntity<Page<Transaction>> getAll(
            Pageable pageable,
            Authentication authentication) {

        return ResponseEntity.ok(
                service.getAll(authentication.getName(), pageable)
        );
    }

    //Filter by Virtual Card
    @GetMapping("/by-card")
    public ResponseEntity<Page<Transaction>> filterByCard(
            @RequestParam Long virtualCardId,
            Pageable pageable,
            Authentication authentication) {

        return ResponseEntity.ok(
                service.filterByCard(
                        authentication.getName(),
                        virtualCardId,
                        pageable
                )
        );
    }

    //  Filter by Merchant Name
    @GetMapping("/by-merchant")
    public ResponseEntity<Page<Transaction>> filterByMerchant(
            @RequestParam String merchant,
            Pageable pageable,
            Authentication authentication) {

        return ResponseEntity.ok(
                service.filterByMerchant(
                        authentication.getName(),
                        merchant,
                        pageable
                )
        );
    }

    //  Filter by Status
    @GetMapping("/by-status")
    public ResponseEntity<Page<Transaction>> filterByStatus(
            @RequestParam TransactionStatus status,
            Pageable pageable,
            Authentication authentication) {

        return ResponseEntity.ok(
                service.filterByStatus(
                        authentication.getName(),
                        status,
                        pageable
                )
        );
    }

    //  Get Transaction By ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                service.getById(id, authentication.getName())
        );
    }


}
