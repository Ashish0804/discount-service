package com.monk.discount.foreign;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
public class Product {
    @NonNull
    private final Long id;
    @NonNull
    private final Double value;
    @NonNull
    private final Integer quantity;

    public Product(final @NonNull Long id, final @NonNull Double value, final @NonNull Integer quantity) {
        if (!(value > 0.0) || value.isInfinite()) {
            throw new IllegalArgumentException("Cannot create product with invalid value");
        }

        this.quantity = quantity;
        this.id = id;
        this.value = value;
    }
}
