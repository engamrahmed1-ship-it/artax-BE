package com.artax.crm.dto.get;

import java.time.LocalDate;

public record CompanyContactDto(
        Long id,
        String title,
        String firstName,
        String secondName,
        String lastName,
        String jobTitle,
        Integer priority,
        String relation,
        String idType,
        String email,
        Long phone,
        String idNumber,
        LocalDate birthdate
) { }
