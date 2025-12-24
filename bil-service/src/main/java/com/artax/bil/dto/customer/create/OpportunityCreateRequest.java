package com.artax.bil.dto.customer.create;

import java.math.BigDecimal;

public record OpportunityCreateRequest(Long customerId, String name, BigDecimal amount,
                                       String stage, BigDecimal probability, java.time.LocalDate closeDate) {
}
