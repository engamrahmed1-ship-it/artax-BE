package com.artax.bil.dto.customer.get;

import java.math.BigDecimal;

public record ProjectDetailDto(Long projectSubId, Long projectId, String subName, String status, java.time.LocalDateTime createdAt, java.time.LocalDateTime closedAt, Integer priority, BigDecimal budget) {}

