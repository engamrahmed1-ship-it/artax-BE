package com.artax.crm.mapper;


import com.artax.crm.dto.create.ProjectCreateRequest;
import com.artax.crm.dto.create.ProjectDetailsCreateRequest;
import com.artax.crm.dto.get.ProjectDetailDto;
import com.artax.crm.dto.get.ProjectDto;
import com.artax.crm.entity.Project;
import com.artax.crm.entity.ProjectDetail;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = { java.time.LocalDateTime.class })
public interface ProjectMapper {

    @Mapping(source = "customer.customerId", target = "customerId")
    ProjectDto toProjectDto(Project project);

    @Mapping(target = "status", defaultValue = "OPEN")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    Project toProject(ProjectCreateRequest dto);

    ProjectDetailDto toDetailDto(ProjectDetail entity);

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    ProjectDetail toDetail(ProjectDetailsCreateRequest dto);
}
