package com.artax.crm.mapper;

import com.artax.crm.dto.create.InteractionCreateRequest;
import com.artax.crm.dto.get.InteractionDto;
import com.artax.crm.entity.Interaction;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InteractionMapper {

    @Mapping(target = "customerId", source = "customer.customerId")
    InteractionDto toInteractionDto(Interaction e);

    @Mapping(target = "interactionDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "customer", ignore = true)
    Interaction toInteraction(InteractionCreateRequest dto);
}

