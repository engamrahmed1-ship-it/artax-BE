package com.artax.crm.controller;

import com.artax.crm.dto.create.LeadCreateRequest;
import com.artax.crm.dto.get.LeadDto;
import com.artax.crm.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/lead")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    @PreAuthorize("hasAnyRole('admin','lead-edit')")
    public ResponseEntity<LeadDto> createLead(@RequestBody LeadCreateRequest dto) {
        return ResponseEntity.ok(leadService.createLead(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin','lead-edit')")
    public ResponseEntity<LeadDto> updateLead(@PathVariable Long id, @RequestBody LeadDto dto) {
        return ResponseEntity.ok(leadService.updateLead(id, dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('admin','lead-list','lead-edit')")
    public ResponseEntity<Page<LeadDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(leadService.getAll(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin','lead-list','lead-edit')")
    public ResponseEntity<LeadDto> getLead(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getById(id));
    }
}
