package com.artax.bil.controller;


import com.artax.bil.dto.customer.create.ProjectCreateRequest;
import com.artax.bil.dto.customer.create.ProjectDetailsCreateRequest;
import com.artax.bil.dto.customer.get.PaginationSearchResponse;
import com.artax.bil.dto.customer.get.ProjectDetailDto;
import com.artax.bil.dto.customer.get.ProjectDto;
import com.artax.bil.dto.customer.update.ProjectDetailUpdateDto;
import com.artax.bil.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/customer/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // --- PROJECT OPERATIONS ---

    // --- READ OPERATIONS ---

    @GetMapping("/all/{customerId}")
    public Mono<ResponseEntity<PaginationSearchResponse>> getProjectsByCustomer(
            @PathVariable Long customerId,
            @RequestHeader("Authorization") String auth,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "20") int size){

        return projectService.getProjectsByCustomerId(auth, customerId,page,size)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{projectId}")
    public Mono<ResponseEntity<ProjectDto>> getProjectById(
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String auth) {
        return projectService.getProjectByProjectId(auth, projectId)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/add/{customerId}")
    public Mono<ResponseEntity<ProjectDto>> addProject(@PathVariable Long customerId,
                                                       @RequestBody ProjectCreateRequest request,
                                                       @RequestHeader("Authorization") String auth) {
        System.out.println("Creating new project for Customer: " + customerId);
        return projectService.addProject(auth, customerId, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/delete/{projectId}")
    public Mono<ResponseEntity<Void>> deleteProject(@PathVariable Long projectId,
                                                    @RequestHeader("Authorization") String auth) {
        return projectService.deleteProject(auth, projectId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    // --- PROJECT DETAIL OPERATIONS ---

    @PostMapping("/detail/add/{projectId}")
    public Mono<ResponseEntity<ProjectDetailDto>> addProjectDetail(@PathVariable Long projectId,
                                                                   @RequestBody ProjectDetailsCreateRequest request,
                                                                   @RequestHeader("Authorization") String auth) {
        return projectService.addProjectDetail(auth, projectId, request)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/detail/update/{detailId}")
    public Mono<ResponseEntity<ProjectDetailDto>> updateProjectDetail(@PathVariable Long detailId,
                                                                      @RequestBody ProjectDetailUpdateDto request,
                                                                      @RequestHeader("Authorization") String auth) {
        return projectService.updateProjectDetail(auth, detailId, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/detail/delete/{detailId}")
    public Mono<ResponseEntity<Void>> deleteProjectDetail(@PathVariable Long detailId,
                                                          @RequestHeader("Authorization") String auth) {
        return projectService.deleteProjectDetail(auth, detailId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}