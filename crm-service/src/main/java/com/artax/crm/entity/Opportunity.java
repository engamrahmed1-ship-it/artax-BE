package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "t_opportunity")
@Getter @Setter @NoArgsConstructor
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opportunityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String name;
    @Column(precision = 14, scale = 2)
    private BigDecimal amount;
    private String stage; // e.g., PROSPECT, PROPOSAL, WON, LOST ,Discovery, Negotiation , Closing
    @Column(precision = 5, scale = 2)
    private BigDecimal probability;
    private LocalDateTime createdAt;
    private LocalDate closeDate;
}
