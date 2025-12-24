package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_crm_interactions")
@Getter @Setter @NoArgsConstructor
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String interactionType; // e.g., CALL, EMAIL
    @Column(length = 1024)
    private String notes;
    private String channel; // WEB, PHONE, CHAT
    private String agentId;
    private LocalDateTime interactionDate;
}
