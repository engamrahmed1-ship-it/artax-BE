package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_project_details")
@Getter @Setter @NoArgsConstructor
public class ProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectSubId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String subName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private Integer priority;
    @Column(precision = 14, scale = 2)
    private BigDecimal budget;
}
