package com.artax.bil.dto.customer.get;

import java.util.List;

public record CustomerProfileDto(PaginationSearchResponse  customer,

                                 PaginationSearchResponse projects,

                                 PaginationSearchResponse opportunities,

                                 PaginationSearchResponse tickets,

                                 PaginationSearchResponse interactions) {
}
