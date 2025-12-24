package com.artax.crm.mapper;


import com.artax.crm.dto.create.OpportunityCreateRequest;
import com.artax.crm.dto.get.OpportunityDto;
import com.artax.crm.entity.Opportunity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OpportunityMapper {

    @Mapping(source = "customer.customerId", target = "customerId")
    OpportunityDto toOpportunityDto(Opportunity e);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "customerId", target = "customer.customerId")
    @Mapping(target = "customer", ignore = true)
    Opportunity toOpportunity(OpportunityCreateRequest dto);


    void updateOpportunityFromDto(OpportunityCreateRequest dto, @MappingTarget Opportunity entity);
}
