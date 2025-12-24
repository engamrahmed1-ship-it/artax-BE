package com.artax.bil.dto.customer.get;

import java.math.BigDecimal;

public record ProjectDto(Long projectId, Long customerId, String projectName,
                         String projectType, String status, java.time.LocalDateTime createdAt,
                         java.time.LocalDateTime closedAt, BigDecimal budget,
                         java.util.List<ProjectDetailDto> details) {}

