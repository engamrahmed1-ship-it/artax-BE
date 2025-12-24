package com.artax.crm.service.impl;


import com.artax.crm.dto.create.InteractionCreateRequest;
import com.artax.crm.dto.get.InteractionDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.entity.Customer;
import com.artax.crm.entity.Interaction;
import com.artax.crm.mapper.InteractionMapper;
import com.artax.crm.repository.CustomerRepository;
import com.artax.crm.repository.InteractionRepository;
import com.artax.crm.service.InteractionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactionRepository;
    private final CustomerRepository customerRepository;
    private final InteractionMapper interactionMapper;

    @Override
    @Transactional
    public InteractionDto addInteraction(Long customerId, InteractionCreateRequest dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Interaction entity = interactionMapper.toInteraction(dto);
        entity.setCustomer(customer);

        return interactionMapper.toInteractionDto(interactionRepository.save(entity));

    }

    @Override
    public PaginationSearchResponse getCustomerInteractions(Long customerId, int page, int size) {
        Page<Interaction> interactionPage = interactionRepository.findByCustomerCustomerId(
                customerId,
                PageRequest.of(page, size, Sort.by("interactionId").descending())
        );
        // Map entities to DTOs (which implement ICustomer)
        List<InteractionDto> dtos = interactionPage.getContent().stream()
                .map(interactionMapper::toInteractionDto)
                .toList();
        System.out.println("the Returned Interactions"+dtos);
        return new PaginationSearchResponse(
                interactionPage.getTotalElements(),
                interactionPage.getTotalPages(),
                interactionPage.getNumber(),
                interactionPage.getSize(),
                dtos,
                200
        );

    }

    @Override
    public void delete(Long interactionId) {
        if (!interactionRepository.existsById(interactionId)) {
            throw new EntityNotFoundException("Interaction not found");
        }
        interactionRepository.deleteById(interactionId);
    }


}
