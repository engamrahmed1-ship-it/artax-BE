package com.artax.bil.controller;

import com.artax.bil.dto.customer.create.CompanyContactRequest;
import com.artax.bil.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/customer/contact")
public class CustomerContactController {

    private final CustomerService customerService;

    public CustomerContactController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/add/{customerId}")
    public Mono<ResponseEntity<Void>> addContact(@PathVariable Long customerId,
                                                 @RequestBody CompanyContactRequest request,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        System.out.println("Customer ID Has a new Contact "+customerId);
//        String authHeader = servletRequest.getHeader("Authorization");
        return customerService.addContact(authHeader, customerId, request)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/delete/{customerId}/{contactId}")
    public Mono<ResponseEntity<Void>> deleteContact(@PathVariable Long customerId,
                                                    @PathVariable Long contactId,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
//        String authHeader = servletRequest.getHeader("Authorization");
        return customerService.deleteContact(authHeader, customerId, contactId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @PutMapping("/primary/{customerId}/{contactId}")
    public Mono<ResponseEntity<Void>> setPrimaryContact(@PathVariable Long customerId,
                                                        @PathVariable Long contactId,
                                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        System.out.println("update Customer Primary"+customerId +" : :: "+contactId);
//        String authHeader = servletRequest.getHeader("Authorization");
        return customerService.setPrimaryContact(authHeader, customerId, contactId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}
