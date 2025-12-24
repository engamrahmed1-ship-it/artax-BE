package com.artax.product.service;

import com.artax.product.Dto.ProductRequest;
import com.artax.product.Dto.ProductResponse;
import com.artax.product.mapper.ProductMapper;
import com.artax.product.entity.Product;
import com.artax.product.infrastructure.IProduct;
import com.artax.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public IProduct create(ProductRequest productRequest){

        System.out.println("Requset : "+productRequest);

        Product prodEntity =  productMapper.ProductDtoToProduct(productRequest);

        System.out.println("Entity : "+prodEntity);

        productRepository.save(prodEntity);
        log.info("Product Created Successfully ");

        ProductResponse productResponse =  productMapper.ProductToProductDto(prodEntity);
        System.out.println("Response : "+productResponse);
        return productResponse;
    }

    public List<ProductResponse> findAll() {

        List<ProductResponse> productResponses = new ArrayList<>();

        productResponses = productRepository.findAll().stream().map(product
                -> productMapper.ProductToProductDto( product )).toList();


        return productResponses;
//        return productRepository.findAll().stream().map(
//                product -> new ProductResponse(product.getId(),
//                        product.getName(), product.getDescription(), product.getTotalPrice())).toList();
    }
}
