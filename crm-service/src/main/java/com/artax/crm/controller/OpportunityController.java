package com.artax.crm.controller;


import com.artax.crm.dto.create.OpportunityCreateRequest;
import com.artax.crm.dto.get.OpportunityDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.service.OpportunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/opportunity")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService service;


    @PreAuthorize("hasAnyRole('customer-edit','admin')")
    @PostMapping
    public ResponseEntity<OpportunityDto> create(@RequestBody OpportunityCreateRequest dto) {
        System.out.println("this is the opp"+dto);
        return ResponseEntity.ok(service.create(dto));
    }

    @PreAuthorize("hasAnyRole('opportunity-edit','opportunity-list', 'admin')")
    @GetMapping("/{id}")
    public ResponseEntity<OpportunityDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasRole('opportunity-list') or hasRole('admin')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<PaginationSearchResponse> listByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.listByCustomer(customerId, page, size));
    }

    @PreAuthorize("hasAnyRole('opportunity-edit', 'admin')")
    @PutMapping("/{opportunityId}")
    public ResponseEntity<OpportunityDto> update(@PathVariable Long opportunityId,
                                                 @RequestBody OpportunityCreateRequest dto) {
        return ResponseEntity.ok(service.update(opportunityId, dto));
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{opportunityId}")
    public ResponseEntity<Void> delete(@PathVariable Long opportunityId) {
        service.delete(opportunityId);
        return ResponseEntity.noContent().build();
    }
}
