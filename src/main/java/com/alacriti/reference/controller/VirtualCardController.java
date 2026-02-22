package com.alacriti.reference.controller;

import com.alacriti.reference.entity.VirtualCard;
import com.alacriti.reference.model.type.CardStatus;
import com.alacriti.reference.service.VirtualCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/virtual-cards")
@RequiredArgsConstructor
public class VirtualCardController {

    private final VirtualCardService service;

    //  Create Virtual Card
    @PostMapping("/{paymentCardId}")
    public ResponseEntity<VirtualCard> create(
            @PathVariable Long paymentCardId,
            Authentication authentication) {

        String email = authentication.getName();
        VirtualCard card = service.create(email, paymentCardId);

        return ResponseEntity.ok(card);
    }

    //  Get All Virtual Cards (Authenticated User)
    @GetMapping
    public ResponseEntity<List<VirtualCard>> getAll(Authentication authentication) {

        String email = authentication.getName();
        List<VirtualCard> cards = service.getAll(email);

        return ResponseEntity.ok(cards);
    }

    //  Get Virtual Card by ID (Must belong to user)
    @GetMapping("/{id}")
    public ResponseEntity<VirtualCard> getById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        VirtualCard card = service.getById(id, email);

        return ResponseEntity.ok(card);
    }

    @PatchMapping("/{id}/limit")
    public ResponseEntity<VirtualCard> updateLimit(
            @PathVariable Long id,
            @RequestParam BigDecimal limit,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(service.updateLimit(id, limit, email));
    }

    @PatchMapping("/{id}/expiry")
    public ResponseEntity<VirtualCard> updateExpiry(
            @PathVariable Long id,
            @RequestParam String expiryDate,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(
                service.updateExpiry(id, LocalDate.parse(expiryDate), email));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VirtualCard> updateStatus(
            @PathVariable Long id,
            @RequestParam CardStatus status,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(service.updateStatus(id, status, email));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<VirtualCard>> filterByStatus(
            @RequestParam CardStatus status,
            Authentication authentication) {

        return ResponseEntity.ok(
                service.filterByStatus(status, authentication.getName())
        );
    }

}