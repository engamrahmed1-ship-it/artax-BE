package com.artax.crm.repository;

import com.artax.crm.entity.CustomerIndividual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerIndividualRepository extends JpaRepository<CustomerIndividual,Long> {
}
