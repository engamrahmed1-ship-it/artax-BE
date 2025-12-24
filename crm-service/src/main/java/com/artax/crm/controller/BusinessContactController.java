package com.artax.crm.controller;

import com.artax.crm.dto.create.CompanyContactRequest;
import com.artax.crm.dto.get.CompanyContactDto;
import com.artax.crm.service.BusinessContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/customer/{customerId}/contact")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('customer-edit','admin')")
public class BusinessContactController {

    private final BusinessContactService service;

    @PostMapping
    public ResponseEntity<CompanyContactDto> addContact(
            @PathVariable Long customerId,
            @RequestBody @Valid CompanyContactRequest dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.addContact(customerId, dto));
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable Long customerId,
            @PathVariable Long contactId) {

        service.deleteContact(customerId, contactId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/primary/{contactId}")
    public ResponseEntity<Void> setPrimaryContact(
            @PathVariable Long customerId,
            @PathVariable Long contactId) {

        System.out.println("Contact ID "+contactId);
        service.setPrimaryContact(customerId, contactId);
        return ResponseEntity.ok().build();
    }
}
