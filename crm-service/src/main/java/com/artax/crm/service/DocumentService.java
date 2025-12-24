package com.artax.crm.service;


import com.artax.crm.dto.create.DocumentCreateRequest;
import com.artax.crm.dto.get.DocumentDto;
import com.artax.crm.dto.get.PaginationSearchResponse;

public interface DocumentService {
    DocumentDto addDocument(Long customerId, DocumentCreateRequest dto);
    PaginationSearchResponse getDocuments(Long customerId, int page, int size);
    public void delete(Long documentID);
}
