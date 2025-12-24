package com.artax.crm.dto.create;

public record DocumentCreateRequest(
        Long customerId, Long projectId, String documentName, String documentType,
        String documentPath
) {
}
