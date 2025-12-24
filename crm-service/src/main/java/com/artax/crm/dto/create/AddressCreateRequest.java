package com.artax.crm.dto.create;

public record AddressCreateRequest(
        String street,
        String city,
        String state,
        Long zipCode,
        String country
) {
}
