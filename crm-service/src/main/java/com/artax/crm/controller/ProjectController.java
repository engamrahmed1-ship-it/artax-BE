package com.artax.crm.controller;

import com.artax.crm.dto.create.ProjectCreateRequest;
import com.artax.crm.dto.create.ProjectDetailsCreateRequest;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.dto.get.ProjectDetailDto;
import com.artax.crm.dto.get.ProjectDto;
import com.artax.crm.dto.update.ProjectDetailUpdateDto;
import com.artax.crm.service.impl.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectServiceImpl service;

    @PostMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('admin','customer-edit')")
    public ResponseEntity<ProjectDto> createProject(
            @PathVariable Long customerId,
            @RequestBody ProjectCreateRequest dto) {
        System.out.println("Creating Project in Progress "+dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createProject(customerId, dto));
    }

    @PostMapping("/{projectId}/detail")
    @PreAuthorize("hasAnyRole('admin','customer-edit')")
    public ResponseEntity<ProjectDetailDto> addDetail(
            @PathVariable Long projectId,
            @RequestBody ProjectDetailsCreateRequest dto) {

        return ResponseEntity.ok(service.addProjectDetail(projectId, dto));
    }


    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('admin','customer-list','customer-edit')")
    public ResponseEntity<PaginationSearchResponse> getProjectsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(service.getProjects(customerId, page, size));

    }

    @GetMapping("/{projectId}")
    @PreAuthorize("hasAnyRole('admin','customer-list')")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(service.getProjectById(projectId));
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasAnyRole('admin','customer-edit')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        service.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/detail/{projectSubId}")
    @PreAuthorize("hasAnyRole('admin','customer-edit')")
    public ResponseEntity<Void> deleteProjectDetail(@PathVariable Long projectSubId) {
        service.deleteProjectDetail(projectSubId);
        return ResponseEntity.noContent().build(); // Returns HTTP 204
    }


    @PutMapping("/detail/{projectSubId}")
    @PreAuthorize("hasAnyRole('admin','customer-edit')")
    public ResponseEntity<ProjectDetailDto> updateDetail(
            @PathVariable Long projectSubId,
            @RequestBody ProjectDetailUpdateDto dto) {

        return ResponseEntity.ok(service.updateProjectDetail(projectSubId, dto));
    }

}
