package com.artax.crm.dto.get;


import java.time.LocalDate;
import java.util.List;

public record CustomerIndividualDto(
        Long individualId,
        String title,
        String firstName,
        String secondName,
        String lastName,
        String custCategory,
        String email,
        String phone,
        String idType,
        String idNumber,
        LocalDate birthdate,
        String gender,
        List<CustomerAddressDto> addresses
) { }

