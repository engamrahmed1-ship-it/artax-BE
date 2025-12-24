package com.artax.bil.dto.customer.create;

public record TicketCreateRequest(Long customerId, String subject,
                                  String description, String priority, String assignedTo) {
}
