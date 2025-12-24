package com.artax.bil.dto.customer.get;

public record InteractionDto(
        Long interactionId, Long customerId, String interactionType, String notes, String channel,
        String agentId,
        java.time.LocalDateTime interactionDate
) {
}
