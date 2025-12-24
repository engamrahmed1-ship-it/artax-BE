package com.artax.bil.dto.customer.create;

import java.time.LocalDate;

public record CompanyContactRequest(
        String title,
        String firstName,
        String secondName,
        String thirdName,
        String jobTitle,
        Integer priority,
        String relation,
        String idType,
        String email,
        Long phone,
        String idNumber,
        LocalDate birthdate
) {
}
