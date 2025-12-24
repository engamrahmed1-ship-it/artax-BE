package com.artax.crm.dto.create;

import java.math.BigDecimal;

public record OpportunityCreateRequest(Long customerId, String name, BigDecimal amount,
                                       String stage, BigDecimal probability, java.time.LocalDate closeDate) {
}
