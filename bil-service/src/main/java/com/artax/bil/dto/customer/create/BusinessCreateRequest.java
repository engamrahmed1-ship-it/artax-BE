package com.artax.bil.dto.customer.create;

import java.util.List;

public record BusinessCreateRequest(
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
        List<CompanyContactRequest> contacts
) {


}
