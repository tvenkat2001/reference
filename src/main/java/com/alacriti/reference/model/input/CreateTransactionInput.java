package com.alacriti.reference.model.input;

import com.alacriti.reference.validation.ValidTransaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@ValidTransaction
public class CreateTransactionInput {
    @NotNull(message = "Virtual card id is required")
    private Long virtualCardId;

    @NotBlank(message = "Merchant name is required")
    private String merchantName;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String description;
}
