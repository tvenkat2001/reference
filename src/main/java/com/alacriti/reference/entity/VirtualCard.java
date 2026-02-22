package com.alacriti.reference.entity;

import com.alacriti.reference.model.type.CardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class VirtualCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private PaymentCard paymentCard;

    @Column(unique = true)
    private String virtualCardNumber;

    private BigDecimal spendingLimit = BigDecimal.ZERO;
    private BigDecimal currentSpent = BigDecimal.ZERO;

    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private CardStatus status = CardStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

}