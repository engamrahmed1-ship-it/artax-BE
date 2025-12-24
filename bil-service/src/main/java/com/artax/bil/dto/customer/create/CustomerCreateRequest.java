package com.artax.bil.dto.customer.create;

public record CustomerCreateRequest(
        String custType, // B2B | B2C
        String status,
        IndividualCreateRequest b2c,
        BusinessCreateRequest b2b
) {}