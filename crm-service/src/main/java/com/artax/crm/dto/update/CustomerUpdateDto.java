package com.artax.crm.dto.update;

public record CustomerUpdateDto(
        String status,
        IndividualUpdateDto b2c,
        BusinessUpdateDto b2b
) {
}
