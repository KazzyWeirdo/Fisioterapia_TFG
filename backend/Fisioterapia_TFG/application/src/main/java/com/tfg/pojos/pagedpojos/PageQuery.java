package com.tfg.pojos.pagedpojos;

public record PageQuery(int page, int size, String sortField, String sortDir) {
    public PageQuery(int page, int size) {
        this(page, size, null, "ASC");
    }
}
