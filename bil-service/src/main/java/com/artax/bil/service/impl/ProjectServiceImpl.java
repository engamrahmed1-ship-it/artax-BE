package com.artax.bil.service.impl;

import com.artax.bil.client.CrmClient;
import com.artax.bil.dto.customer.create.ProjectCreateRequest;
import com.artax.bil.dto.customer.create.ProjectDetailsCreateRequest;
import com.artax.bil.dto.customer.get.PaginationSearchResponse;
import com.artax.bil.dto.customer.get.ProjectDetailDto;
import com.artax.bil.dto.customer.get.ProjectDto;
import com.artax.bil.dto.customer.update.ProjectDetailUpdateDto;
import com.artax.bil.service.ProjectService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final CrmClient crmClient;

    public ProjectServiceImpl(CrmClient crmClient) {
        this.crmClient = crmClient;
    }



//    @Override
//    public Mono<PaginationSearchResponse> getProjectsByCustomerId(String auth, Long customerId) {
//        Mono<PaginationSearchResponse> list= crmClient.getProjectsByCustomer(auth, customerId);
//
//        System.out.println("the Returned Data From BIL"+list);
//        return list;
//    }

    @Override
    public Mono<PaginationSearchResponse> getProjectsByCustomerId(String auth, Long customerId
            ,int page , int size) {
        return crmClient.getProjectsByCustomer(auth, customerId,page,size)
                .doOnNext(response -> {
                    // This will execute ONLY when the data actually arrives from CRM
                    System.out.println("The Returned Data From BIL: " + response.data());
                    System.out.println("Total Items: " + response.totalCount());
                })
                .doOnError(error -> System.err.println("Error fetching projects: " + error.getMessage()));
    }


    @Override
    public Mono<ProjectDto> getProjectByProjectId(String auth, Long projectId) {
        return crmClient.getProjectById(auth, projectId)
                .doOnError(e -> System.out.println("Error retrieving project {}: {}"
                        + projectId+ e.getMessage()));
    }

    /**
     * Creates a project and potentially triggers notifications or downstream events
     */
    public Mono<ProjectDto> addProject(String authHeader, Long customerId, ProjectCreateRequest dto) {
        return crmClient.createProject(authHeader, customerId, dto)
                .doOnSuccess(project -> {
                    // Optional: Trigger a "Project Started" event via Kafka if needed
                })
                .doOnError(e -> System.out.println("Failed to create project in CRM: {}"
                        + e.getMessage()));
    }

    @Override
    public Mono<Void> deleteProject(String auth, Long projectId) {
        System.out.println("Processing BIL Request: Delete Project {}"+ projectId);
        return crmClient.deleteProject(auth, projectId);
    }



    @Override
    public Mono<ProjectDetailDto> addProjectDetail(String auth, Long projectId, ProjectDetailsCreateRequest request) {
        System.out.println("Processing BIL Request: Add Sub-task to Project {}"+ projectId);
        return crmClient.addProjectDetail(auth, projectId, request);
    }

    @Override
    public Mono<ProjectDetailDto> updateProjectDetail(String auth, Long detailId, ProjectDetailUpdateDto request) {
        System.out.println("Processing BIL Request: Update Sub-task {}"+ detailId);
        return crmClient.updateProjectDetail(auth, detailId, request);
    }

    @Override
    public Mono<Void> deleteProjectDetail(String auth, Long detailId) {
        System.out.println("Processing BIL Request: Delete Sub-task {}"+ detailId);
        return crmClient.deleteProjectDetail(auth, detailId);
    }


}
