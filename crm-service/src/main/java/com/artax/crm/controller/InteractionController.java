package com.artax.crm.controller;


import com.artax.crm.dto.create.InteractionCreateRequest;
import com.artax.crm.dto.get.InteractionDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.service.impl.InteractionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionServiceImpl service;

    @PostMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('admin','customer-edit')")
    public ResponseEntity<InteractionDto> createInteraction(
            @PathVariable Long customerId,
            @RequestBody InteractionCreateRequest dto) {

        System.out.println("Creating Project in Progress "+dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addInteraction(customerId, dto));
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('admin','customer-list','customer-edit')")
    public ResponseEntity<PaginationSearchResponse> getCustomerInteractions(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(service.getCustomerInteractions(customerId, page, size));
    }

    @DeleteMapping("/{interactionId}")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<Void> deleteInteraction(@PathVariable Long interactionId) {
        service.delete(interactionId);
        return ResponseEntity.noContent().build();
    }
}
