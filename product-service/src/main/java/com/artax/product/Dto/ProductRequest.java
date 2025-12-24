package com.artax.product.Dto;

import com.artax.product.infrastructure.IProduct;

import java.math.BigDecimal;

public record ProductRequest(String id, String name, String description, BigDecimal totalPrice) implements IProduct {
}
