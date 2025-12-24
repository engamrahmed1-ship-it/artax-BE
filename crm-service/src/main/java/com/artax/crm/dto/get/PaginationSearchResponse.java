package com.artax.crm.dto.get;




import java.util.List;

public record PaginationSearchResponse<T>(
        Long totalCount,
        Integer totalPages,      // ← Added this
        Integer currentPage,     // ← Added this
        Integer pageSize,
        List<T> data, // Generic list to hold any DTO
        int httpStatus
) {
}
