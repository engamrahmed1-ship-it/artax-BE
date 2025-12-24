package com.artax.crm.dto.create;

public record TicketCreateRequest(Long customerId, String subject,
                                  String description, String priority, String assignedTo) {
}
