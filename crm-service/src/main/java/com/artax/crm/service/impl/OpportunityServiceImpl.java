package com.artax.crm.service.impl;


import com.artax.crm.dto.create.OpportunityCreateRequest;
import com.artax.crm.dto.get.OpportunityDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.entity.Customer;
import com.artax.crm.entity.Opportunity;
import com.artax.crm.mapper.OpportunityMapper;
import com.artax.crm.repository.CustomerRepository;
import com.artax.crm.repository.OpportunityRepository;
import com.artax.crm.service.OpportunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository repo;
    private final OpportunityMapper mapper;
    private final CustomerRepository customerRepository;

    @Override
    public OpportunityDto create(OpportunityCreateRequest dto) {
        Opportunity entity = mapper.toOpportunity(dto);
        if (dto.customerId() != null) {
            Customer c = customerRepository.findById(dto.customerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            entity.setCustomer(c);
        }
        Opportunity saved = repo.save(entity);
        return mapper.toOpportunityDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OpportunityDto getById(Long id) {
        return repo.findById(id).map(mapper::toOpportunityDto).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationSearchResponse listByCustomer(Long customerId, int page, int size) {
        Page<Opportunity> opportunityPage = repo.findByCustomerCustomerId(
                customerId,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
        // Map entities to DTOs (which implement ICustomer)
        List<OpportunityDto> dtos = opportunityPage.getContent().stream()
                .map(mapper::toOpportunityDto)
                .toList();
        System.out.println("the Returned Opportunities"+dtos);
        return new PaginationSearchResponse(
                opportunityPage.getTotalElements(),
                opportunityPage.getTotalPages(),
                opportunityPage.getNumber(),
                opportunityPage.getSize(),
                dtos,
                200
        );
    }

    @Override
    public OpportunityDto update(Long id, OpportunityCreateRequest dto) {
        // 1. Fetch the existing record
        Opportunity ex = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found with id: " + id));
        // 2. Map the DTO changes onto the existing entity
        mapper.updateOpportunityFromDto(dto, ex);
        // 3. Save and return the result
        return mapper.toOpportunityDto(repo.save(ex));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
