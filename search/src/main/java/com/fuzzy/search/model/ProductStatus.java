package com.fuzzy.search.model;

import java.util.Optional;

public enum ProductStatus {
    SALE,
    INSPECTING,
    NOTPUBLISHED,
    OUTOFSTOCK,
    SUSPENDED,
    PREPARED,
    ACTIVE,
    DELETE,
    NONE;

    public static ProductStatus from(String text) {
        if (text == null) {
            return NONE;
        } else {
            return Optional.of(ProductStatus.valueOf(text.toUpperCase()))
                    .orElse(ProductStatus.NONE); // or another default or undefined value
        }
    }
}
