package com.artax.crm.service.impl;

import com.artax.crm.dto.create.TicketCreateRequest;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.dto.get.TicketDto;
import com.artax.crm.entity.Customer;
import com.artax.crm.entity.Ticket;
import com.artax.crm.mapper.TicketMapper;
import com.artax.crm.repository.CustomerRepository;
import com.artax.crm.repository.TicketRepository;
import com.artax.crm.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final TicketMapper mapper;

    @Override
    @Transactional
    public TicketDto createTicket(Long customerId, TicketCreateRequest dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Ticket ticket = mapper.toTicket(dto);
        ticket.setCustomer(customer);

        return mapper.toTicketDto(ticketRepository.save(ticket));
    }

    @Override
    public PaginationSearchResponse getTickets(Long customerId, int page, int size) {
        Page<Ticket> ticketPage = ticketRepository.findByCustomerCustomerId(
                customerId,
                PageRequest.of(page, size, Sort.by("ticketId").descending())
        );
        List<TicketDto> dtos = ticketPage.getContent().stream()
                .map(mapper::toTicketDto)
                .toList();
        System.out.println("the Returned Tickets"+dtos);
        return new PaginationSearchResponse(
                ticketPage.getTotalElements(),
                ticketPage.getTotalPages(),
                ticketPage.getNumber(),
                ticketPage.getSize(),
                dtos,
                200
        );
    }

    @Override
    public void delete(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new EntityNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(ticketId);
    }
}
