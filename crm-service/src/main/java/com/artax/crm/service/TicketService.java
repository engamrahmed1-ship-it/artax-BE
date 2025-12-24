package com.artax.crm.service;


import com.artax.crm.dto.create.TicketCreateRequest;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.dto.get.TicketDto;

public interface TicketService {
    TicketDto createTicket(Long customerId, TicketCreateRequest dto);
    PaginationSearchResponse getTickets(Long customerId, int page, int size);
    public void delete(Long ticketId);
}
