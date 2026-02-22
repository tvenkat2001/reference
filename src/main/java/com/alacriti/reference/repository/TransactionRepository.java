package com.alacriti.reference.repository;

import com.alacriti.reference.entity.Transaction;
import com.alacriti.reference.model.type.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByVirtualCardUserEmail(String email, Pageable pageable);

    Page<Transaction> findByVirtualCardUserEmailAndVirtualCardId(
            String email, Long cardId, Pageable pageable);

    Page<Transaction> findByVirtualCardUserEmailAndMerchantNameContainingIgnoreCase(
            String email, String merchant, Pageable pageable);

    Page<Transaction> findByVirtualCardUserEmailAndStatus(
            String email, TransactionStatus status, Pageable pageable);

    Optional<Transaction> findByIdAndVirtualCardUserEmail(
            Long id, String email);
}
