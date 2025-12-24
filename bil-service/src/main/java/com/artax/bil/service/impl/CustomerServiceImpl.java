package com.artax.bil.service.impl;

import com.artax.bil.client.CrmClient;
import com.artax.bil.dto.customer.create.CompanyContactRequest;
import com.artax.bil.dto.customer.create.CustomerCreateRequest;
import com.artax.bil.dto.customer.get.*;
import com.artax.bil.service.CustomerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {


    private final CrmClient  crmClient;
    private final KafkaTemplate<String, Object> kafka;


    public CustomerServiceImpl(CrmClient crmClient, KafkaTemplate<String, Object> kafka) {
        this.crmClient = crmClient;
        this.kafka = kafka;
    }


    @Override
    public Mono<CustomerDto> createCustomer(String authHeader, CustomerCreateRequest req) {
        Mono<CustomerDto> customerDto = crmClient.createCustomer(authHeader,req);
        return  customerDto;
//                .flatMap(response -> {
//                    kafka.send("crm.customer.created.v1",
//                            response.customerId().toString(), response);
//                    return Mono.just(response);
//                });
//        return crmClient.createCustomer(authHeader, req)
//                .doOnSuccess(dto -> kafka.send("crm.customer.created.v1", dto.getCustomerId().toString(), dto));
    }

    @Override
    public  Mono<PaginationSearchResponse> search
            (String authHeader,Long id,String name,
             String phone, String custType, int page, int size) {
        return crmClient.searchCustomers(authHeader,id, name, phone, custType, page, size);
//                .flatMap(response -> {
//                    kafka.send("crm.customer.created.v1", response.customerId().toString(), response);
//                    return Mono.just(response);
//                });

    }

    // Optimized Profile Aggregation: All calls run in parallel
    @Override
    public Mono<CustomerProfileDto> getCustomerProfile(String authHeader, Long customerId) {
        return Mono.zip(
                crmClient.searchCustomers(authHeader, customerId,null,null,null,0,20)
                        .log("CUSTOMER-SERVICE")
                        .timeout(Duration.ofSeconds(3)),
                crmClient.getProjectsByCustomer(authHeader, customerId,0,20)
                        .log("PROJECT-SERVICE")
                        .timeout(Duration.ofSeconds(3)),
                crmClient.getOpportunitesByCustomer(authHeader, customerId,0,20)
                        .log("OPPORTUNITES-SERVICE")
                        .timeout(Duration.ofSeconds(3)),
                crmClient.getTickets(authHeader, customerId,0,20)
                        .log("TICKET-SERVICE")
                        .timeout(Duration.ofSeconds(3)),
                crmClient.getCustomerInteractions(authHeader, customerId,0,20)
                        .log("INTERACTION-SERVICE")
                        .timeout(Duration.ofSeconds(3))
        ).map(tuple -> new CustomerProfileDto(
                tuple.getT1(), // Customer
                tuple.getT2(), // Projects
                tuple.getT3(), // Opportunities
                tuple.getT4(), // Tickets
                tuple.getT5()  // Interactions
        ));
    }

//    Contacts  For Business

    @Override
    public Mono<Void> addContact(String authHeader, Long customerId, CompanyContactRequest request) {
        return crmClient.addContact(authHeader, customerId, request).then();
    }

    @Override
    public Mono<Void> deleteContact(String authHeader, Long customerId, Long contactId) {
        return crmClient.deleteContact(authHeader, customerId, contactId).then();
    }

    @Override
    public Mono<Void> setPrimaryContact(String authHeader, Long customerId, Long contactId) {
        return crmClient.setPrimaryContact(authHeader, customerId, contactId).then();
    }



}