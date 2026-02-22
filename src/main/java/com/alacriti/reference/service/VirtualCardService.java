package com.alacriti.reference.service;

import com.alacriti.reference.entity.PaymentCard;
import com.alacriti.reference.entity.VirtualCard;
import com.alacriti.reference.model.type.CardStatus;
import com.alacriti.reference.repository.PaymentCardRepository;
import com.alacriti.reference.repository.VirtualCardRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VirtualCardService {

    private final VirtualCardRepository repository;
    private final PaymentCardRepository paymentCardRepository;

    @Transactional
    public VirtualCard create(String userEmail, Long paymentCardId) {

        PaymentCard paymentCard = paymentCardRepository.findById(paymentCardId)
                .orElseThrow(() -> new RuntimeException("Payment card not found"));

        // Ensure card belongs to logged-in user
        if (!paymentCard.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        VirtualCard card = new VirtualCard();
        card.setUser(paymentCard.getUser());
        card.setPaymentCard(paymentCard);
        card.setVirtualCardNumber(generateCardNumber());
        card.setStatus(CardStatus.ACTIVE);
        card.setCreatedAt(LocalDateTime.now());

        return repository.save(card);
    }

    @Transactional(readOnly = true)
    public List<VirtualCard> getAll(String email) {
        return repository.findByUserEmail(email);
    }

    @Transactional(readOnly = true)
    public VirtualCard getById(Long id, String email) {
        return repository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    @Transactional(readOnly = true)
    private String generateCardNumber() {
        String number;
        do {
            number = "4" + RandomStringUtils.randomNumeric(15);
        } while (repository.existsByVirtualCardNumber(number));
        return number;
    }

    @Transactional
    public VirtualCard updateLimit(Long id, BigDecimal limit, String email) {

        VirtualCard card = repository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setSpendingLimit(limit);

        return repository.save(card);
    }

    @Transactional
    public VirtualCard updateExpiry(Long id, LocalDate expiry, String email) {

        VirtualCard card = repository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setExpiryDate(expiry);

        return repository.save(card);
    }

    @Transactional
    public VirtualCard updateStatus(Long id, CardStatus status, String email) {

        VirtualCard card = repository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setStatus(status);

        return repository.save(card);
    }

    public List<VirtualCard> filterByStatus(CardStatus status,
                                            String email) {

        return repository
                .findByUserEmailAndStatus(email, status);
    }
}