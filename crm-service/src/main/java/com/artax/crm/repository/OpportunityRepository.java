package com.artax.crm.repository;

import com.artax.crm.entity.Opportunity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    Page<Opportunity> findByCustomerCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT SUM(o.amount) FROM Opportunity o " +
            "WHERE o.customer.customerId = :custId " +
            "AND o.stage NOT IN ('WON', 'LOST')")
    BigDecimal calculateCustomerPipeline(@Param("custId") Long customerId);
}
