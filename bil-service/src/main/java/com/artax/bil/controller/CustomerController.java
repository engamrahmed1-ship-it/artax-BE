package com.artax.bil.controller;

import com.artax.bil.dto.customer.create.CustomerCreateRequest;
import com.artax.bil.dto.customer.get.CustomerDto;
import com.artax.bil.dto.customer.get.CustomerProfileDto;
import com.artax.bil.dto.customer.get.PaginationSearchResponse;
import com.artax.bil.service.impl.CustomerServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest; // REFACTORED: Use Reactive Request
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/customer")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Mono<ResponseEntity<CustomerDto>> create(
            @RequestBody CustomerCreateRequest request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) { // REFACTORED: Direct Header Injection

        return customerService.createCustomer(authHeader, request)
                .map(ResponseEntity::ok)
                .doOnNext(res -> System.out.println("Customer Created Successfully"));
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<PaginationSearchResponse>> search(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String custType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) { // REFACTORED: Direct Header Injection

        return customerService.search(authHeader, id, name, phone, custType, page, size)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/profile/{id}")
    public Mono<ResponseEntity<CustomerProfileDto>> getProfile(
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) { // REFACTORED: Direct Header Injection

        System.out.println("Getting Info For Profile ID " + id);

        return customerService.getCustomerProfile(authHeader, id)
                .map(ResponseEntity::ok);
    }
}