package com.artax.crm.dto.create;

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
        String idNumber,
        String email,
        Long phone,
        LocalDate birthdate
) {
}
