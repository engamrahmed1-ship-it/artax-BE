package com.artax.bil.dto.customer.create;

public record InteractionCreateRequest(
        Long customerId, String interactionType, String notes,
                                   String channel,
                                   String agentId) {
}
