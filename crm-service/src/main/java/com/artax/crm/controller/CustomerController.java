package com.artax.crm.controller;

import com.artax.crm.dto.create.CustomerCreateRequest;
import com.artax.crm.dto.get.CustomerDto;
import com.artax.crm.dto.get.PaginationSearchResponse;
import com.artax.crm.service.impl.CustomerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/customer")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerServiceImpl customerServiceImpl;

    @PreAuthorize("hasAnyRole('customer-list', 'admin')")
    @GetMapping("/search")
    public ResponseEntity<PaginationSearchResponse> search(HttpServletRequest request,
                                                           @RequestParam(name = "id", required = false) Long id,
                                                           @RequestParam(name = "name", required = false) String name,
                                                           @RequestParam(name = "phone", required = false) String phone,
                                                           @RequestParam(name = "custType", required = false) String custType,
                                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                                           @RequestParam(name = "size", defaultValue = "20") int size) {


        System.out.println("=== DEBUG QUERY STRING ===");
        System.out.println("Full URL: " + request.getRequestURL() + "?" + request.getQueryString());
        System.out.println("Raw query string: " + request.getQueryString());
        System.out.println("id from @RequestParam: " + id);
        System.out.println("id from request.getParameter('id'): " + request.getParameter("id"));
        System.out.println("All parameters: " + request.getParameterMap());
        System.out.println("==========================");

        // If id is provided → fetch by ID
        if (id != null) {
            return ResponseEntity.ok(customerServiceImpl.getById(id));
        }


        return ResponseEntity.ok(customerServiceImpl.searchCustomers(name, phone, custType,page, size));
    }






//    @PreAuthorize("hasAnyRole('customer-list', 'admin')")
//    @GetMapping("/{id}")
//    public ResponseEntity<PaginationSearchResponse> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(customerServiceImpl.getById(id));
//    }

    @PreAuthorize("hasAnyRole('customer-list', 'admin')")
    @GetMapping
    public ResponseEntity<PaginationSearchResponse> getAll(
            @RequestParam(required = false) String custType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(customerServiceImpl.searchCustomers(null, null, custType,page, size));
    }


    /////////////////////////////



    @PreAuthorize("hasAnyRole('customer-edit', 'admin')")
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CustomerCreateRequest dto) {
        System.out.println("request"+dto);
        CustomerDto created = customerServiceImpl.createCustomer(dto);

        System.out.println("created"+created);

        return ResponseEntity.status(201).body(created);
    }


//    @PreAuthorize("hasAnyRole('customer-edit', 'admin')")
//    @PutMapping("/{id}")
//    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerUpdateDto dto) {
//        return ResponseEntity.ok(customerServiceImpl.updateCustomer(id, dto));
//    }


}

