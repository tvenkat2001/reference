package com.alacriti.reference;

import com.alacriti.reference.entity.VirtualCard;
import com.alacriti.reference.model.input.CreateTransactionInput;
import com.alacriti.reference.repository.VirtualCardRepository;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Documented
@Constraint(validatedBy = ValidTransaction.Validator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTransaction {

    String message() default "Invalid transaction request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class Validator implements ConstraintValidator<ValidTransaction, CreateTransactionInput> {

        private final VirtualCardRepository virtualCardRepository;

        @Override
        public boolean isValid(CreateTransactionInput input,
                               ConstraintValidatorContext context) {

            if (input.getVirtualCardId() == null || input.getAmount() == null) {
                return true; // handled by field validation
            }

            VirtualCard card = virtualCardRepository
                    .findById(input.getVirtualCardId())
                    .orElse(null);

            if (card == null) {
                return buildError(context, "Virtual card not found");
            }

            if (card.getStatus() != com.alacriti.reference.model.type.CardStatus.ACTIVE) {
                return buildError(context, "Card is not active");
            }

            if (card.getExpiryDate() != null &&
                    card.getExpiryDate().isBefore(LocalDate.now())) {
                return buildError(context, "Card is expired");
            }

            BigDecimal newSpending =
                    card.getCurrentSpent().add(input.getAmount());

            if (card.getSpendingLimit() != null &&
                    newSpending.compareTo(card.getSpendingLimit()) > 0) {
                return buildError(context, "Spending limit exceeded");
            }

            return true;
        }

        private boolean buildError(ConstraintValidatorContext context, String message) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
    }
}
