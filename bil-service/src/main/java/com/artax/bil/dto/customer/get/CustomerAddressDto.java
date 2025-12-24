package com.artax.bil.dto.customer.get;

public record CustomerAddressDto(
        Long id,
        String street,
        String city,
        String state,
        Long zipCode,
        String country
) { }