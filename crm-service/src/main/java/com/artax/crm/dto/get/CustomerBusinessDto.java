package com.artax.crm.dto.get;

import java.util.List;

public record CustomerBusinessDto(
        Long companyId,
        String companyName,
        String street,
        String city,
        String state,
        Long zipCode,
        String country,
        String commercialRegister,
        String website,
        String companyClass,
        String industry,
        String companySize,
        Long primaryContactId,
        List<CompanyContactDto> contacts
) {
    }




