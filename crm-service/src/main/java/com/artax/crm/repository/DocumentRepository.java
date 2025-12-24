package com.artax.crm.repository;

import com.artax.crm.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByCustomerCustomerId(Long customerId, Pageable pageable);
    Page<Document> findByProjectProjectId(Long projectId, Pageable pageable);
}
