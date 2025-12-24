package com.artax.bil.service;

import com.artax.bil.dto.customer.create.CompanyContactRequest;
import com.artax.bil.dto.customer.create.CustomerCreateRequest;
import com.artax.bil.dto.customer.get.CustomerDto;
import com.artax.bil.dto.customer.get.CustomerProfileDto;
import com.artax.bil.dto.customer.get.PaginationSearchResponse;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<CustomerDto> createCustomer(String authHeader, CustomerCreateRequest dto);

    Mono<PaginationSearchResponse> search(
            String authHeader,
            Long id,
            String name,
            String phone,
            String custType,
            int page,
            int size
    );

     Mono<CustomerProfileDto> getCustomerProfile(String authHeader, Long customerId);

    Mono<Void> addContact(String authHeader, Long customerId, CompanyContactRequest request);
    Mono<Void> deleteContact(String authHeader, Long customerId, Long contactId);
    Mono<Void> setPrimaryContact(String authHeader, Long customerId, Long contactId);
}