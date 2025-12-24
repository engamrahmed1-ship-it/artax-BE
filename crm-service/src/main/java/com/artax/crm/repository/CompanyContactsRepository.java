package com.artax.crm.repository;

import com.artax.crm.entity.CompanyContacts;
import com.artax.crm.entity.CustomerBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyContactsRepository  extends JpaRepository<CompanyContacts,Long> {

    @Query("SELECT c FROM CompanyContacts c WHERE c.id = :contactId AND c.company = :company")
    Optional<CompanyContacts> findByIdAndCompany(Long contactId, CustomerBusiness company);
}
