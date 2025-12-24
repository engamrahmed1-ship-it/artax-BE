package com.artax.bil.dto.customer.create;

public record AddressCreateRequest(
        String street,
        String city,
        String state,
        Long zipCode,
        String country
) {
}
