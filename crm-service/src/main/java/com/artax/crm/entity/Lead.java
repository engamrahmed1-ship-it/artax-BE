package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_lead")
@Getter @Setter @NoArgsConstructor
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leadId;

    private String source; // WEB, TRADE_SHOW, REFERRAL
    private String status; // NEW, QUALIFIED, CONVERTED
    private String assignedTo;
    private LocalDateTime createdAt;

    // optional: customer once converted
    @OneToOne
    @JoinColumn(name = "converted_customer_id")
    private Customer convertedCustomer;
}
