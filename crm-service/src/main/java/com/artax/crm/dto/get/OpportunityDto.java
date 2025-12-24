package com.artax.crm.dto.get;

import java.math.BigDecimal;

public record OpportunityDto(Long opportunityId, Long customerId, String name,
                             BigDecimal amount, String stage,
                             BigDecimal probability,
                             java.time.LocalDateTime createdAt, java.time.LocalDate closeDate) {
}
