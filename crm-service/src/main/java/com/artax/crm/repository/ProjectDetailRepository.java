package com.artax.crm.repository;

import com.artax.crm.entity.ProjectDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Long> {
    Page<ProjectDetail> findByProjectProjectId(Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"project"})
    Optional<ProjectDetail> findWithProjectByProjectSubId(Long projectSubId);
}
