package com.tfg.pojos.pagedpojos;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int pageNumber,
        boolean isLast
) {}
