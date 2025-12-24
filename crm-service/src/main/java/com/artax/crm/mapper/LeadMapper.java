package com.artax.crm.mapper;


import com.artax.crm.dto.create.LeadCreateRequest;
import com.artax.crm.dto.get.LeadDto;
import com.artax.crm.entity.Lead;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LeadMapper {

    LeadDto toLeadDto(Lead e);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "convertedCustomer", ignore = true)
    Lead toLead(LeadCreateRequest dto);

    void updateLead(LeadDto dto,@MappingTarget Lead lead);
}
