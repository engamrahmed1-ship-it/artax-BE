package com.artax.crm.dto.get;

public record LeadDto(
        Long leadId, String source, String status,
                      String assignedTo, java.time.LocalDateTime createdAt,
        Long convertedCustomerId) {
}
