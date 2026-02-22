package com.alacriti.reference.repository;

import com.alacriti.reference.entity.VirtualCard;
import com.alacriti.reference.model.type.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface VirtualCardRepository extends JpaRepository<VirtualCard, Long> {

    List<VirtualCard> findByUserEmail(String email);

    Optional<VirtualCard> findByIdAndUserEmail(Long id, String email);

    boolean existsByVirtualCardNumber(String number);

    List<VirtualCard> findByUserEmailAndStatus(String email, CardStatus status);


}