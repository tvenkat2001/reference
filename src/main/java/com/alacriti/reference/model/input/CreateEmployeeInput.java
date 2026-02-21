package com.alacriti.reference.model.input;

import lombok.Builder;

@Builder
public record CreateEmployeeInput(
        String firstName,
        String lastName,
        String email,
        Double salary,
        String department
) {
}
