package com.artax.product.controller;

import com.artax.product.Dto.ProductRequest;
import com.artax.product.Dto.ProductResponse;
import com.artax.product.infrastructure.IProduct;
import com.artax.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public IProduct createProduct(@RequestBody ProductRequest productRequest) {

          return productService.create(productRequest);
    }



    @GetMapping(value = {"", "/"})
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('product-list', 'admin')")
    public List<ProductResponse>  getAllProducts()
    {
            return productService.findAll();
    }

}
