package com.artax.bil.dto.customer.get;



import java.util.List;

public record PaginationSearchResponse<T>(
        Long totalCount,
        Integer totalPages,      // ← Added this
        Integer currentPage,     // ← Added this
        Integer pageSize,
        List<T> data,
        int httpStatus
) {
    //    Static helper for empty responses
    public static <T> PaginationSearchResponse<T> empty() {
        return new PaginationSearchResponse<>(0L, 0, 0, 20, List.of(), 200);
    }

}