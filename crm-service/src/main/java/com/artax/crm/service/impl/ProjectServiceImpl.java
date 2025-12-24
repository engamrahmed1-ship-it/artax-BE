package com.artax.crm.service.impl;

import com.artax.crm.dto.create.ProjectCreateRequest;
import com.artax.crm.dto.create.ProjectDetailsCreateRequest;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.dto.get.ProjectDetailDto;
import com.artax.crm.dto.get.ProjectDto;
import com.artax.crm.dto.update.ProjectDetailUpdateDto;
import com.artax.crm.entity.Customer;
import com.artax.crm.entity.Project;
import com.artax.crm.entity.ProjectDetail;
import com.artax.crm.mapper.ProjectMapper;
import com.artax.crm.repository.CustomerRepository;
import com.artax.crm.repository.ProjectDetailRepository;
import com.artax.crm.repository.ProjectRepository;
import com.artax.crm.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDetailRepository projectDetailRepository;
    private final CustomerRepository customerRepository;
    private final ProjectMapper projectMapper;



    @Override
    @Transactional
    public ProjectDto createProject(Long customerId, ProjectCreateRequest dto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new EntityNotFoundException("Customer not found")
        );

        Project project = projectMapper.toProject(dto);
        project.setCustomer(customer);

        return projectMapper.toProjectDto(projectRepository.save(project));
    }



    @Override
    @Transactional
    public ProjectDetailDto addProjectDetail(Long projectId, ProjectDetailsCreateRequest dto) {
        Project project = projectRepository.findById(projectId).
                orElseThrow(() -> new EntityNotFoundException("Project not found")
        );

        ProjectDetail detail = projectMapper.toDetail(dto);
        detail.setProject(project);

        return projectMapper.toDetailDto(projectDetailRepository.save(detail));
    }


    @Override
    @Transactional(readOnly = true)
    public PaginationSearchResponse getProjects(Long customerId, int page, int size) {
        Page<Project> projectPage = projectRepository.findAllByCustomerCustomerId(
                customerId,
                PageRequest.of(page, size, Sort.by("projectId").descending())
        );

        // Map entities to DTOs (which implement ICustomer)
        List<ProjectDto> dtos = projectPage.getContent().stream()
                .map(projectMapper::toProjectDto)
                .toList();

        System.out.println("the Returned Projects"+dtos);

        return new PaginationSearchResponse(
                projectPage.getTotalElements(),
                projectPage.getTotalPages(),
                projectPage.getNumber(),
                projectPage.getSize(),
                dtos,
                200
        );
    }

    @Override
    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project not found");
        }
        projectRepository.deleteById(projectId);
    }

    @Transactional(readOnly = true)
    @Override
    public ProjectDto getProjectById(Long projectId) {
        return projectRepository.findWithDetailsByProjectId(projectId)
                .map(projectMapper::toProjectDto) // You'll need this mapping in your Mapper
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
    }

    @Override
    public void deleteProjectDetail(Long projectSubId) {
        if (!projectDetailRepository.existsById(projectSubId)) {
            throw new EntityNotFoundException("Project Detail not found with ID: " + projectSubId);
        }
        projectDetailRepository.deleteById(projectSubId);
    }


    public ProjectDetailDto updateProjectDetail(Long projectSubId, ProjectDetailUpdateDto dto) {
        ProjectDetail detail = projectDetailRepository.findWithProjectByProjectSubId(projectSubId)
                .orElseThrow(() -> new EntityNotFoundException("Detail not found"));

        if (dto.status() != null) {
            detail.setStatus(dto.status());
            if ("COMPLETED".equalsIgnoreCase(dto.status())) {
                detail.setClosedAt(LocalDateTime.now());
            } else {
                detail.setClosedAt(null); // Reset if moved back from Completed
            }
        }

        if (dto.subName() != null) detail.setSubName(dto.subName());
        if (dto.priority() != null) detail.setPriority(dto.priority());
        if (dto.budget() != null) detail.setBudget(dto.budget());

        // Save the detail first so the recalculation sees the fresh data
        ProjectDetail savedDetail = projectDetailRepository.save(detail);

        // Sync Budget and Status to the Parent Project
        syncProjectWithDetails(savedDetail.getProject());

        return projectMapper.toDetailDto(savedDetail);
    }

    private void syncProjectWithDetails(Project project) {
        List<ProjectDetail> details = project.getDetails();
        if (details == null || details.isEmpty()) return;

        // 1. Recalculate Budget
        BigDecimal totalBudget = details.stream()
                .map(ProjectDetail::getBudget)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        project.setBudget(totalBudget);

        // 2. Logic for Project Status
        long totalCount = details.size();
        long completedCount = details.stream().filter(d -> "COMPLETED".equalsIgnoreCase(d.getStatus())).count();
        long openCount = details.stream().filter(d -> "OPEN".equalsIgnoreCase(d.getStatus())).count();

        if (completedCount == totalCount) {
            // SCENARIO: All in COMPLETED Status -> CLOSED
            project.setStatus("CLOSED");
        } else if (openCount == totalCount) {
            // SCENARIO: All in OPEN Status -> OPEN
            project.setStatus("OPEN");
        } else {
            // SCENARIO: Any task is IN-PROGRESS or some are COMPLETED but not all
            project.setStatus("IN_PROGRESS");
        }

        projectRepository.save(project);
    }




}

