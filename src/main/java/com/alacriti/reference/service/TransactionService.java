package com.alacriti.reference.service;


import com.alacriti.reference.entity.Transaction;
import com.alacriti.reference.entity.VirtualCard;
import com.alacriti.reference.model.type.CardStatus;
import com.alacriti.reference.model.type.TransactionStatus;
import com.alacriti.reference.repository.TransactionRepository;
import com.alacriti.reference.repository.VirtualCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final VirtualCardRepository virtualCardRepository;

    @Transactional
    public Transaction process(Long virtualCardId,
                               String merchant,
                               BigDecimal amount,
                               String description,
                               String email) {

        // 1️⃣ Validate Card Ownership
        VirtualCard card = virtualCardRepository
                .findByIdAndUserEmail(virtualCardId, email)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        Transaction tx = new Transaction();
        tx.setVirtualCard(card);
        tx.setMerchantName(merchant);
        tx.setAmount(amount);
        tx.setDescription(description);
        tx.setCreatedAt(LocalDateTime.now());

        // 2️⃣ Card must be ACTIVE
        if (card.getStatus() != CardStatus.ACTIVE) {
            tx.setStatus(TransactionStatus.FAILED);
            tx.setFailureReason("Card is not active");
            return repository.save(tx);
        }

        // 3️⃣ Card must not be expired
        if (card.getExpiryDate() != null &&
                card.getExpiryDate().isBefore(LocalDate.now())) {

            tx.setStatus(TransactionStatus.FAILED);
            tx.setFailureReason("Card is expired");
            return repository.save(tx);
        }

        // 4️⃣ Spending limit validation
        BigDecimal currentSpent = card.getCurrentSpent() == null
                ? BigDecimal.ZERO
                : card.getCurrentSpent();

        BigDecimal spendingLimit = card.getSpendingLimit() == null
                ? BigDecimal.ZERO
                : card.getSpendingLimit();

        BigDecimal newTotal = currentSpent.add(amount);

        if (newTotal.compareTo(spendingLimit) > 0) {

            tx.setStatus(TransactionStatus.FAILED);
            tx.setFailureReason("Spending limit exceeded");
            return repository.save(tx);
        }

        // 6️⃣ SUCCESS case
        card.setCurrentSpent(newTotal);
        tx.setStatus(TransactionStatus.SUCCESS);

        // Save transaction
        return repository.save(tx);
    }


    @Transactional
    public Page<Transaction> getAll(String email, Pageable pageable) {
        return repository.findByVirtualCardUserEmail(email, pageable);
    }

    @Transactional
    public Page<Transaction> filterByCard(String email,
                                          Long cardId,
                                          Pageable pageable) {

        return repository
                .findByVirtualCardUserEmailAndVirtualCardId(
                        email, cardId, pageable);
    }

    @Transactional
    public Page<Transaction> filterByMerchant(String email,
                                              String merchant,
                                              Pageable pageable) {

        return repository
                .findByVirtualCardUserEmailAndMerchantNameContainingIgnoreCase(
                        email, merchant, pageable);
    }

    @Transactional
    public Page<Transaction> filterByStatus(String email,
                                            TransactionStatus status,
                                            Pageable pageable) {

        return repository
                .findByVirtualCardUserEmailAndStatus(
                        email, status, pageable);
    }

    @Transactional
    public Transaction getById(Long id, String email) {

        return repository
                .findByIdAndVirtualCardUserEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}


