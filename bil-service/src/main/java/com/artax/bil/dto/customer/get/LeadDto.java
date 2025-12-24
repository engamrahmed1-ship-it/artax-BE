package com.artax.bil.dto.customer.get;

public record LeadDto(
        Long leadId, String source, String status,
                      String assignedTo, java.time.LocalDateTime createdAt,
        Long convertedCustomerId) {
}
