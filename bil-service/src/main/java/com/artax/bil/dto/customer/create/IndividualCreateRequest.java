package com.artax.bil.dto.customer.create;

import java.time.LocalDate;
import java.util.List;

public record IndividualCreateRequest(
        String title,
        String firstName,
        String secondName,
        String lastName,
        String custCategory,
        String email,
        Long phone,
        String idType,
        String idNumber,
        LocalDate birthdate,
        String gender,
        List<AddressCreateRequest> addresses
) {

}
