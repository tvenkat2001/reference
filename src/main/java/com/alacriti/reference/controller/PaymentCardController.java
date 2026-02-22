package com.alacriti.reference.controller;

import com.alacriti.reference.entity.PaymentCard;
import com.alacriti.reference.service.PaymentCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-cards")
@RequiredArgsConstructor
public class PaymentCardController {

    private final PaymentCardService service;

    @PostMapping
    public PaymentCard create(
            @RequestBody PaymentCard card,
            Authentication authentication) {

        String email = authentication.getName();
        return service.create(card, email);
    }

    @GetMapping
    public List<PaymentCard> getAll(Authentication authentication) {

        String email = authentication.getName();
        return service.getUserCards(email);
    }
}