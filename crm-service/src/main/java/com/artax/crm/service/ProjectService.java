package com.artax.crm.service;
import com.artax.crm.dto.create.ProjectCreateRequest;
import com.artax.crm.dto.create.ProjectDetailsCreateRequest;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.dto.get.ProjectDetailDto;
import com.artax.crm.dto.get.ProjectDto;
import com.artax.crm.dto.update.ProjectDetailUpdateDto;

public interface ProjectService {
    ProjectDto createProject(Long customerId, ProjectCreateRequest dto);
    ProjectDetailDto addProjectDetail(Long projectId, ProjectDetailsCreateRequest dto);
     PaginationSearchResponse getProjects(Long customerId, int page, int size);
     void deleteProject(Long projectId);
     ProjectDto getProjectById(Long projectId);
     void deleteProjectDetail(Long projectSubId);

     ProjectDetailDto updateProjectDetail(Long projectSubId, ProjectDetailUpdateDto dto);
}
