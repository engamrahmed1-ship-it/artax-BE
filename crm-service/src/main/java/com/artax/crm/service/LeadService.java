package com.artax.crm.service;


import com.artax.crm.dto.create.LeadCreateRequest;
import com.artax.crm.dto.get.LeadDto;
import org.springframework.data.domain.Page;

public interface LeadService {
    LeadDto createLead(LeadCreateRequest dto);
    LeadDto updateLead(Long id, LeadDto dto);
    Page<LeadDto> getAll(int page, int size);
    LeadDto getById(Long id);
}
