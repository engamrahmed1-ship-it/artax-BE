package com.artax.crm.controller;

import com.artax.crm.dto.create.DocumentCreateRequest;
import com.artax.crm.dto.get.DocumentDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    @PostMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('admin','customer-edit')")
    public ResponseEntity<DocumentDto> addDocument(
            @PathVariable Long customerId,
            @RequestBody DocumentCreateRequest dto) {

        return ResponseEntity.ok(service.addDocument(customerId, dto));
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('admin','customer-list','customer-edit')")
    public ResponseEntity<PaginationSearchResponse> getDocuments(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(service.getDocuments(customerId, page, size));
    }

    @DeleteMapping("/{documentId}")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        service.delete(documentId);
        return ResponseEntity.noContent().build();
    }
}
