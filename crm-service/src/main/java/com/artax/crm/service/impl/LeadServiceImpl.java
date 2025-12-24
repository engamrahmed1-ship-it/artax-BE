package com.artax.crm.service.impl;

import com.artax.crm.dto.create.LeadCreateRequest;
import com.artax.crm.dto.get.LeadDto;
import com.artax.crm.entity.*;
import com.artax.crm.mapper.LeadMapper;
import com.artax.crm.repository.CustomerRepository;
import com.artax.crm.repository.LeadRepository;
import com.artax.crm.repository.OpportunityRepository;
import com.artax.crm.service.LeadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final CustomerRepository customerRepository;
    private final OpportunityRepository opportunityRepository;
    private final LeadMapper leadMapper;

    @Override
    @Transactional
    public LeadDto createLead(LeadCreateRequest dto) {
        Lead lead = leadMapper.toLead(dto);
        return leadMapper.toLeadDto(leadRepository.save(lead));
    }

    @Override
    @Transactional
    public LeadDto updateLead(Long id, LeadDto dto) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        leadMapper.updateLead(dto, lead);
        return leadMapper.toLeadDto(leadRepository.save(lead));
    }

    @Override
    public Page<LeadDto> getAll(int page, int size) {
        return leadRepository.findAll(PageRequest.of(page, size))
                .map(leadMapper::toLeadDto);
    }

    @Override
    public LeadDto getById(Long id) {
        return leadMapper.toLeadDto(
                leadRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Lead not found"))
        );
    }



    public void convertLead(Long leadId, String type, String oppName, BigDecimal amount) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new EntityNotFoundException("Lead not found"));

        // 1. Create the Base Customer
        Customer customer = new Customer();
        customer.setCustType(type); // "B2B" or "B2C"
        customer.setStatus("ACTIVE");
        customer.setDateJoined(LocalDate.now());

        // We must save the parent first to generate the ID
        customer = customerRepository.save(customer);

        // 2. Create the Specific Sub-type
        if ("B2B".equalsIgnoreCase(type)) {
            CustomerBusiness business = new CustomerBusiness();
            business.setCustomer(customer);
            business.setCompanyName(lead.getSource()); // Or lead.getCompanyName()
            // Map other B2B fields...
            customer.setB2b(business);
        } else {
            CustomerIndividual individual = new CustomerIndividual();
            individual.setCustomer(customer);
            individual.setFirstName("Lead Name"); // Map from lead
            // Map other B2C fields...
            customer.setB2c(individual);
        }

        // 3. Create the Opportunity
        Opportunity opp = new Opportunity();
        opp.setCustomer(customer);
        opp.setName(oppName);
        opp.setAmount(amount);
        opp.setStage("Discovery");
        opp.setProbability(new BigDecimal("10.00"));
        opp.setCreatedAt(LocalDateTime.now());
        opportunityRepository.save(opp);

        // 4. Close the Lead
        lead.setStatus("CONVERTED");
        lead.setConvertedCustomer(customer);
        leadRepository.save(lead);
    }
}
