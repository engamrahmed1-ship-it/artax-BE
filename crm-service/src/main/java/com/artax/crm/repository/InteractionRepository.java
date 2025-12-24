package com.artax.crm.repository;

import com.artax.crm.entity.Interaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    Page<Interaction> findByCustomerCustomerId(Long customerId, Pageable pageable);
}
