package com.artax.crm.service;

import com.artax.crm.dto.create.CompanyContactRequest;
import com.artax.crm.dto.get.CompanyContactDto;

public interface BusinessContactService {

    CompanyContactDto addContact(Long companyId, CompanyContactRequest dto);

    void deleteContact(Long companyId, Long contactId);

    void setPrimaryContact(Long companyId, Long contactId);
}
