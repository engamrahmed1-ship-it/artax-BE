package com.artax.crm.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PageUtils {

    /**
     * Merges two separate Page objects into a single Page.
     * Useful when combining results from different repositories (B2B and B2C).
     */
    public static <T> Page<T> mergePages(Page<? extends T> page1, Page<? extends T> page2, Pageable pageable) {
        // 1. Combine the content of both pages
        List<T> combinedContent = Stream.concat(
                page1.getContent().stream(),
                page2.getContent().stream()
        ).collect(Collectors.toList());

        // 2. Calculate the total elements from both sources
        long totalElements = page1.getTotalElements() + page2.getTotalElements();

        // 3. Return a new PageImpl with the merged data
        return new PageImpl<>(combinedContent, pageable, totalElements);
    }
}