package com.artax.crm.service;


import com.artax.crm.dto.create.CustomerCreateRequest;
import com.artax.crm.dto.get.CustomerDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.dto.update.CustomerUpdateDto;

public interface CustomerService {
    PaginationSearchResponse searchCustomers(String name, String phone, String custType, int page, int size);

    PaginationSearchResponse getById(Long id);

    CustomerDto createCustomer(CustomerCreateRequest dto);

    CustomerDto updateCustomer(Long id, CustomerUpdateDto dto);


}
