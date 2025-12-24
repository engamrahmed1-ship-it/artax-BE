package com.artax.crm.dto.create;

import java.math.BigDecimal;

public record ProjectDetailsCreateRequest(Long projectId,
                                          String subName, String status, Integer priority, BigDecimal budget) {}
