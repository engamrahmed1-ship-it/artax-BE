package com.artax.order.dto;

import com.artax.order.infrastructure.IOrder;

import java.math.BigDecimal;

public record OrderRequest(Long id, String orderNumber, String skuCode,
                           BigDecimal price, Integer quantity, UserDetails userDetails)   implements IOrder {
}



