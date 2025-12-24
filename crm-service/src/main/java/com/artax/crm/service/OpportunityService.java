package com.artax.crm.service;


import com.artax.crm.dto.create.OpportunityCreateRequest;
import com.artax.crm.dto.get.OpportunityDto;
import com.artax.crm.dto.get.PaginationSearchResponse;

public interface OpportunityService {
    OpportunityDto create(OpportunityCreateRequest dto);
    OpportunityDto getById(Long id);
    PaginationSearchResponse listByCustomer(Long customerId, int page, int size);
    OpportunityDto update(Long id, OpportunityCreateRequest dto);
    void delete(Long id);
}
