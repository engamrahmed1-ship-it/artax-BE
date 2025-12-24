package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_tickets")
@Getter @Setter @NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "ticket_code", unique = true, nullable = false, updatable = false)
    private String ticketCode;

    private String subject;
    @Column(length = 2048)
    private String description;
    private String priority; // LOW, MEDIUM, HIGH
    private String status;   // OPEN, PENDING, CLOSED
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    private String resolution;
}
