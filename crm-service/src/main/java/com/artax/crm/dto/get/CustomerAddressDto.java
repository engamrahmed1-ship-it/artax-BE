package com.artax.crm.dto.get;

public record CustomerAddressDto(
        Long id,
        String street,
        String city,
        String state,
        Long zipCode,
        String country
) { }