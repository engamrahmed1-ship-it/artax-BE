package com.artax.crm.dto.update;

public record BusinessUpdateDto(
        String street, String city, String website,Long primaryContactId
) {
}
