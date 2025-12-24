package com.artax.bil.dto.customer.create;

import java.math.BigDecimal;

public record ProjectCreateRequest(Long customerId,
                                   String projectName, String projectType,String status, BigDecimal budget) {}

