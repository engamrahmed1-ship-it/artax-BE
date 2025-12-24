package com.artax.crm.mapper;


import com.artax.crm.dto.create.TicketCreateRequest;
import com.artax.crm.dto.get.TicketDto;
import com.artax.crm.entity.Ticket;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketDto toTicketDto(Ticket e);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "customer", ignore = true)
    Ticket toTicket(TicketCreateRequest dto);
}
