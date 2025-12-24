package com.artax.bil.dto.customer.create;

import java.math.BigDecimal;

public record ProjectDetailsCreateRequest(Long projectId,
                                          String subName, String status, Integer priority, BigDecimal budget) {}
