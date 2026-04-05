package com.anush.aiproject.shared.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.anush.aiproject.dto.request.PaginationRequest;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaginationUtil {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int MAX_PAGE_SIZE = 50;

    public Pageable of(PaginationRequest request) {
        if (request == null) {
            return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        }
        return PageRequest.of(
                page(request.getPageNumber()),
                size(request.getPageSize()),
                sort(request.getSort()));
    }

    // helpers
    private int page(Integer page) {
        if (page == null || page < 0) {
            return DEFAULT_PAGE_NUMBER;
        }
        return page;
    }

    private int size(Integer size) {
        if (size == null || size <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, MAX_PAGE_SIZE);
    }

    @NotNull
    private Sort sort(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return Sort.unsorted();
        }

        sortBy = sortBy.trim();

        if (sortBy.startsWith("-")) {
            return Sort.by(sortBy.substring(1)).descending();
        }

        return Sort.by(sortBy).ascending();
    }

}
