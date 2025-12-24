package com.artax.crm.controller;

import com.artax.crm.dto.create.TicketCreateRequest;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.dto.get.TicketDto;
import com.artax.crm.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;

    @PostMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('admin','customer-edit')")
    public ResponseEntity<TicketDto> create(
            @PathVariable Long customerId,
            @RequestBody TicketCreateRequest dto) {

        return ResponseEntity.ok(service.createTicket(customerId, dto));
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('admin','customer-list','customer-edit')")
    public ResponseEntity<PaginationSearchResponse> getTickets(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(service.getTickets(customerId, page, size));
    }

    @DeleteMapping("/{ticketId}")
    @PreAuthorize("hasAnyRole('admin')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        service.delete(ticketId);
        return ResponseEntity.noContent().build();
    }
}
