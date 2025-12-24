package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_company_contacts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyContacts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long id;

    private String title;
    private String firstName;
    private String secondName;
    private String lastName;  //  to be update to lastName
    private String jobTitle;
    private Integer priority;
    private String relation;
    private String idType;
    private String email;
    private Long phone;
    private String idNumber;
    private LocalDate birthdate;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private CustomerBusiness company;
}
