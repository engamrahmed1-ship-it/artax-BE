package com.artax.crm.dto.get;



import java.time.LocalDate;

public record CustomerDto(
        Long customerId,
        String custType,
        String status,
        LocalDate dateJoined,
        CustomerIndividualDto b2c,
        CustomerBusinessDto b2b
//        List<Opportunity> opportunities
     ){
}


