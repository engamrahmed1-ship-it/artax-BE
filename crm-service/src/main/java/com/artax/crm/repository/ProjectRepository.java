package com.artax.crm.repository;

import com.artax.crm.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByCustomerCustomerId(Long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"details"})
    Optional<Project> findWithDetailsByProjectId(Long projectId);


}
