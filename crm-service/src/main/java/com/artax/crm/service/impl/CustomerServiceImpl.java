package com.artax.crm.service.impl;

import com.artax.crm.dto.create.CompanyContactRequest;
import com.artax.crm.dto.create.CustomerCreateRequest;
import com.artax.crm.dto.get.CustomerDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.dto.update.CustomerUpdateDto;
import com.artax.crm.entity.*;
import com.artax.crm.infrastructure.PageUtils;
import com.artax.crm.mapper.CustomerMapper;
import com.artax.crm.repository.CompanyContactsRepository;
import com.artax.crm.repository.CustomerBusinessRepository;
import com.artax.crm.repository.CustomerRepository;
import com.artax.crm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CompanyContactsRepository companyContactsRepository;
    private final CustomerBusinessRepository businessRepository;

    @Transactional(readOnly = true)
    public PaginationSearchResponse searchCustomers(String name, String phone, String custType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> resultPage;

        System.out.println("name"+name);
        System.out.println("phone"+phone);
        System.out.println("custType"+custType);
        System.out.println("page"+page);
        System.out.println("size"+size);
        if ("B2C".equalsIgnoreCase(custType)) {
            if (name != null && !name.isBlank()) {
                System.out.println("1");
                resultPage = customerRepository.searchB2CByName(name.trim(), pageable);
            } else if (phone != null && !phone.isBlank()) {
                System.out.println("2");
                resultPage = customerRepository.searchB2CByPhone(phone.trim(), pageable);
            } else {
                System.out.println("3");
                resultPage = customerRepository.findByCustType("B2C", pageable);
            }
        } else if ("B2B".equalsIgnoreCase(custType)) {
            if (name != null && !name.isBlank()) {
                System.out.println("4");
                resultPage = customerRepository.searchB2BByName(name.trim(), pageable);
            } else if (phone != null && !phone.isBlank()) {
                System.out.println("5");
                resultPage = customerRepository.searchB2BByPhone(phone.trim(), pageable);
            } else {
                System.out.println("6");
                resultPage = customerRepository.findByCustType("B2B", pageable);
            }
        } else {
            // Both B2B & B2C
            if (name != null && !name.isBlank()) {
                System.out.println("7");
                Page<Customer> b2c = customerRepository.searchB2CByName(name.trim(), pageable);
                Page<Customer> b2b = customerRepository.searchB2BByName(name.trim(), pageable);
                resultPage = PageUtils.mergePages(b2c, b2b, pageable);
            } else if (phone != null && !phone.isBlank()) {
                System.out.println("8");
                Page<Customer> b2c = customerRepository.searchB2CByPhone(phone.trim(), pageable);
                Page<Customer> b2b = customerRepository.searchB2BByPhone(phone.trim(), pageable);
                resultPage = PageUtils.mergePages(b2c, b2b, pageable);
            } else {
                System.out.println("9");
                resultPage = customerRepository.findAll(pageable);
            }
        }

        List<CustomerDto> dtoList = resultPage.getContent().stream()
                .map(customerMapper::toCustomerDto)
                .toList();

        return new PaginationSearchResponse(
                resultPage.getTotalElements(),
                resultPage.getTotalPages(),
                page,
                size,
                dtoList,
                200
        );
    }



    @Transactional(readOnly = true)
    public PaginationSearchResponse getById(Long id) {

        Customer customer = customerRepository.findWithDetailsByCustomerId(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return new PaginationSearchResponse(
                1L,
                1,
                0,
                1,
                List.of(customerMapper.toCustomerDto(customer)),
                200
        );
    }




    @Transactional
    public CustomerDto createCustomer(CustomerCreateRequest dto) {
        // Create main customer entity
        Customer entity = new Customer();
        entity.setCustType(dto.custType());
        entity.setStatus(dto.status());
        entity.setDateJoined(LocalDate.now());

        // ------------------ B2C ------------------
        if ("B2C".equalsIgnoreCase(dto.custType()) && dto.b2c() != null) {
            CustomerIndividual ind = customerMapper.toIndividual(dto.b2c(), entity);
            // map addresses
            List<CustomerIndividualAddress> addresses = dto.b2c().addresses().stream()
                    .map(addrDto -> {
                        CustomerIndividualAddress addr = customerMapper.toAddress(addrDto, ind);
                        addr.setCustomerIndividual(ind); // ensure parent is set
                        return addr;
                    })
                    .collect(Collectors.toList());

            ind.setAddresses(addresses);
            entity.setB2c(ind);
            entity.setB2b(null); // ensure no B2B data
            Customer savedCustomer = customerRepository.save(entity);
            return customerMapper.toCustomerDto(savedCustomer);
        }

        // ------------------ B2B ------------------
        else if ("B2B".equalsIgnoreCase(dto.custType()) && dto.b2b() != null) {
            CustomerBusiness bus = customerMapper.toBusiness(dto.b2b(), entity);
            entity.setB2b(bus);
            entity.setB2c(null); // ensure no B2C data

            // Map contacts once and attach to business
            List<CompanyContacts> contacts = new ArrayList<>();
            for (CompanyContactRequest c : dto.b2b().contacts()) {
                CompanyContacts contact = customerMapper.toContact(c, bus);
                contact.setCompany(bus); // link parent
                contacts.add(contact);
            }
            bus.setContacts(contacts);

            // Save parent Customer (cascades business + contacts)
            Customer saved = customerRepository.save(entity);

            // Set primary contact based on priority = 1
            bus.getContacts().stream()
                    .filter(c -> c.getPriority() != null && c.getPriority() == 1)
                    .findFirst()
                    .ifPresent(primary -> bus.setPrimaryContactId(primary.getId()));

            return customerMapper.toCustomerDto(saved);
        }

        throw new IllegalArgumentException("Invalid customer type or missing data");
    }






    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerUpdateDto dto) {
        Customer ex = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        if (dto.status() != null) ex.setStatus(dto.status());
        if (ex.getB2c() != null && dto.b2c() != null) {
            ex.getB2c().setEmail(dto.b2c().email());
            ex.getB2c().setPhone(dto.b2c().phone() != null ? Long.parseLong(dto.b2c().phone()) : ex.getB2c().getPhone());
        }
        if (ex.getB2b() != null && dto.b2b() != null) {
            ex.getB2b().setStreet(dto.b2b().street());
            ex.getB2b().setCity(dto.b2b().city());
        }
        return customerMapper.toCustomerDto(customerRepository.save(ex));
    }



    @Transactional
    public CustomerDto updatePrimaryContact(Long customerId, Long contactId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (customer.getB2b() == null) {
            throw new RuntimeException("Customer is not B2B");
        }

        CustomerBusiness business = customer.getB2b();

        // Validate contact belongs to same company
        CompanyContacts contact = companyContactsRepository
                .findByIdAndCompany(contactId, business)
                .orElseThrow(() -> new RuntimeException(
                        "Contact does not belong to this company"));

        // Update primary contact
        business.setPrimaryContactId(contactId);

        Customer saved = customerRepository.save(customer);

        return customerMapper.toCustomerDto(saved);
    }


}



