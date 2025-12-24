package com.artax.crm.mapper;


import com.artax.crm.dto.create.DocumentCreateRequest;
import com.artax.crm.dto.get.DocumentDto;
import com.artax.crm.entity.Document;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDto toDocumentDto(Document e);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(source = "customerId", target = "customer.customerId")
    @Mapping(source = "projectId", target = "project.projectId")
    Document toDocument(DocumentCreateRequest dto);
}
