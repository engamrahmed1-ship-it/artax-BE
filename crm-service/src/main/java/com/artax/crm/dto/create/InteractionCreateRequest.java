package com.artax.crm.dto.create;

public record InteractionCreateRequest(
        Long customerId, String interactionType, String notes,
                                   String channel,
                                   String agentId) {
}
