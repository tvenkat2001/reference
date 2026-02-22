package com.alacriti.reference.service;

import com.alacriti.reference.entity.PaymentCard;
import com.alacriti.reference.entity.User;
import com.alacriti.reference.repository.PaymentCardRepository;
import com.alacriti.reference.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentCardService {

    private final PaymentCardRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentCard create(PaymentCard card, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        card.setUser(user);

        return repository.save(card);
    }

    @Transactional(readOnly = true)
    public List<PaymentCard> getUserCards(String email) {
        return repository.findByUserEmail(email);
    }
}