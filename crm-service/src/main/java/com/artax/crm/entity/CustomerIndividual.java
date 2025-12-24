package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_customer_individual")
@Getter @Setter
@NoArgsConstructor
public class CustomerIndividual {

        @Id
        private Long individualId;

        @OneToOne
        @MapsId
        @JoinColumn(name = "individual_id")
        private Customer customer;

        private String title;
        private String firstName;
        private String secondName;
        private String lastName;
        private String custCategory;
        private String email;
        private Long phone;
        private String idType;
        private String idNumber;
        private LocalDate birthdate;
        private String gender;

        @OneToMany(mappedBy = "customerIndividual", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<CustomerIndividualAddress> addresses = new ArrayList<>();
}
