package com.artax.crm.service.impl;

import com.artax.crm.dto.create.DocumentCreateRequest;
import com.artax.crm.dto.get.DocumentDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.entity.Customer;
import com.artax.crm.entity.Document;
import com.artax.crm.mapper.DocumentMapper;
import com.artax.crm.repository.CustomerRepository;
import com.artax.crm.repository.DocumentRepository;
import com.artax.crm.service.DocumentService;
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
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository repository;
    private final CustomerRepository customerRepository;
    private final DocumentMapper mapper;

    @Override
    @Transactional
    public DocumentDto addDocument(Long customerId, DocumentCreateRequest dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        var entity = mapper.toDocument(dto);
        entity.setCustomer(customer);

        return mapper.toDocumentDto(repository.save(entity));
    }

    @Override
    public PaginationSearchResponse getDocuments(Long customerId, int page, int size) {
        Page<Document> documentPage = repository.findByCustomerCustomerId(
                customerId,
                PageRequest.of(page, size, Sort.by("documentId").descending())
        );
        // Map entities to DTOs (which implement ICustomer)
        List<DocumentDto> dtos = documentPage.getContent().stream()
                .map(mapper::toDocumentDto)
                .toList();
        System.out.println("the Returned Interactions"+dtos);
        return new PaginationSearchResponse(
                documentPage.getTotalElements(),
                documentPage.getTotalPages(),
                documentPage.getNumber(),
                documentPage.getSize(),
                dtos,
                200
        );
    }

    @Override
    public void delete(Long documentId) {
        if (!repository.existsById(documentId)) {
            throw new EntityNotFoundException("document not found");
        }
        repository.deleteById(documentId);
    }
}
