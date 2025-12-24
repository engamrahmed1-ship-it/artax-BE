package com.artax.crm.repository;

import com.artax.crm.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByCustomerCustomerId(Long customerId, Pageable pageable);
}
