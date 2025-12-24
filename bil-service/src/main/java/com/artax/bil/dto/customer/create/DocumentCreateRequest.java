package com.artax.bil.dto.customer.create;

public record DocumentCreateRequest(
        Long customerId, Long projectId, String documentName, String documentType,
        String documentPath
) {
}
