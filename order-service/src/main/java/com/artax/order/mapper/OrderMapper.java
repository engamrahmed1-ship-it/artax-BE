package com.artax.order.mapper;

import com.artax.order.dto.OrderRequest;
import com.artax.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {UUID.class, BigDecimal.class} // Import UUID and BigDecimal for use in expressions
)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true) // Best practice: Ignore ID from DTO to ensure a new entity is created
    @Mapping(target = "orderNumber", expression = "java(UUID.randomUUID().toString())") // Generates a new UUID
    @Mapping(target = "price", expression = "java(orderRequest.price().multiply(BigDecimal.valueOf(orderRequest.quantity())))") // Calculates total price
    Order orderRequestToOrder(OrderRequest orderRequest);
}
