package com.artax.crm.service.impl;

import com.artax.crm.dto.create.CompanyContactRequest;
import com.artax.crm.dto.get.CompanyContactDto;
import com.artax.crm.entity.CompanyContacts;
import com.artax.crm.entity.CustomerBusiness;
import com.artax.crm.mapper.CustomerMapper;
import com.artax.crm.repository.CompanyContactsRepository;
import com.artax.crm.repository.CustomerBusinessRepository;
import com.artax.crm.service.BusinessContactService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessContactServiceImpl implements BusinessContactService {

    private final CustomerBusinessRepository businessRepo;
    private final CompanyContactsRepository contactRepo;
    private final CustomerMapper mapper;

    @Override
    public CompanyContactDto addContact(Long companyId, CompanyContactRequest dto) {

        CustomerBusiness business = businessRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));

        CompanyContacts contact = mapper.toContact(dto, business);
        contact.setCompany(business);

        CompanyContacts saved = contactRepo.save(contact);

        // If priority == 1 → set as primary
        if (saved.getPriority() != null && saved.getPriority() == 1) {
            business.setPrimaryContactId(saved.getId());
        }

        return mapper.toCompanyContactDto(saved);
    }

    @Override
    public void deleteContact(Long companyId, Long contactId) {

        CustomerBusiness business = businessRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));

        CompanyContacts contact = contactRepo.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        if (!contact.getCompany().getCompanyId().equals(companyId)) {
            throw new IllegalArgumentException("Contact does not belong to business");
        }

        // Unset primary if needed
        if (contactId.equals(business.getPrimaryContactId())) {
            business.setPrimaryContactId(null);
        }

        contactRepo.delete(contact);
    }

    @Override
    public void setPrimaryContact(Long companyId, Long contactId) {

        CustomerBusiness business = businessRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found"));

        CompanyContacts contact = contactRepo.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));

        if (!contact.getCompany().getCompanyId().equals(companyId)) {
            throw new IllegalArgumentException("Contact does not belong to business");
        }

        business.setPrimaryContactId(contact.getId());
    }
}
