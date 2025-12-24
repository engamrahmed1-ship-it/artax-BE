package com.artax.crm.dto.create;

import java.math.BigDecimal;

public record ProjectCreateRequest(Long customerId,
                                   String projectName, String projectType,String status, BigDecimal budget) {}

