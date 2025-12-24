package com.artax.bil.dto.customer.get;



import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerDto(
        Long customerId,
        String custType,
        String status,
        LocalDate dateJoined,
        CustomerIndividualDto b2c,
        CustomerBusinessDto b2b
//        List<Opportunity> opportunities
     ) {
}


