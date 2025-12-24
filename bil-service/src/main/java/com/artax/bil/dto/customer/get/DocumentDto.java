package com.artax.bil.dto.customer.get;

public record DocumentDto(
        Long documentId, Long customerId, Long projectId,
                          String documentName, String documentType, java.time.LocalDateTime createdAt,
        String documentPath
        ) {
}
