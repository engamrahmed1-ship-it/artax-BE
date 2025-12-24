package com.artax.bil.dto.customer.update;

import java.math.BigDecimal;

public record ProjectDetailUpdateDto(
         String subName,
         String status,
         Integer priority,
         BigDecimal budget
) {
}
