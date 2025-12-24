package com.artax.bil.service;

import com.artax.bil.dto.customer.create.ProjectCreateRequest;
import com.artax.bil.dto.customer.create.ProjectDetailsCreateRequest;
import com.artax.bil.dto.customer.get.PaginationSearchResponse;
import com.artax.bil.dto.customer.get.ProjectDetailDto;
import com.artax.bil.dto.customer.get.ProjectDto;
import com.artax.bil.dto.customer.update.ProjectDetailUpdateDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface ProjectService {

    public Mono<PaginationSearchResponse> getProjectsByCustomerId(String auth, Long customerId ,int page , int size);
    public Mono<ProjectDto> getProjectByProjectId(String auth, Long projectId);
    public Mono<ProjectDto> addProject(String authHeader, Long customerId, ProjectCreateRequest dto);

    public Mono<Void> deleteProject(String auth, Long projectId);


    public Mono<ProjectDetailDto> addProjectDetail(String auth, Long projectId, ProjectDetailsCreateRequest request);
    public Mono<ProjectDetailDto> updateProjectDetail(String auth, Long detailId, ProjectDetailUpdateDto request);
    public Mono<Void> deleteProjectDetail(String auth, Long detailId);
}
