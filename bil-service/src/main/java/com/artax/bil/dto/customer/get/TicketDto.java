package com.artax.bil.dto.customer.get;

public record TicketDto(
        Long ticketId, Long customerId,String ticketCode, String subject,
        String description, String priority, String status,
        String assignedTo, java.time.LocalDateTime createdAt, java.time.LocalDateTime closedAt,
        String resolution
) {
}
