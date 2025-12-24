package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_customer_business")
@Getter @Setter
@NoArgsConstructor
public class CustomerBusiness {

        @Id
        private Long companyId;

        @OneToOne
        @MapsId
        @JoinColumn(name = "company_id")
        private Customer customer;

        private String companyName;
        private String street;
        private String city;
        private String state;
        private Long zipCode;
        private String country;
        private String commercialRegister;
        private String website;
        private String companyClass;
        private String industry;
        private String companySize;
        private Long primaryContactId;

        @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
        private List<CompanyContacts> contacts = new ArrayList<>();


}
