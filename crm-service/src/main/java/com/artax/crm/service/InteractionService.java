package com.artax.crm.service;


import com.artax.crm.dto.create.InteractionCreateRequest;
import com.artax.crm.dto.get.InteractionDto;
import com.artax.crm.dto.get.PaginationSearchResponse;

public interface InteractionService {
    InteractionDto addInteraction(Long customerId, InteractionCreateRequest dto);
    PaginationSearchResponse getCustomerInteractions(Long customerId, int page, int size);
    public void delete(Long interactionId);
}
