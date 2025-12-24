package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="t_customer_individual_address")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerIndividualAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    private String street;
    private String city;
    private String state;
    private Long zipCode;
    private String country;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "individual_id")
    private CustomerIndividual customerIndividual;
}
