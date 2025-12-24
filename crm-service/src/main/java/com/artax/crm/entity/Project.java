package com.artax.crm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_projects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String projectName;
    private String projectType;
    private String status;    //OPEN: ONCE CREATED   IN-PROGRESS: if any task IN PROGRESS  CLOSED : if ALL tasks CLOSED

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime closedAt;

    @Column(precision = 14, scale = 2)
    private BigDecimal budget;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectDetail> details = new ArrayList<>();

    // Helper method to keep bi-directional relationship in sync
    public void addDetail(ProjectDetail detail) {
        details.add(detail);
        detail.setProject(this);
    }
}
