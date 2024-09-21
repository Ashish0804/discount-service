package com.monk.discount.foreign;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Set;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class Cart {
    private final List<Product> items;

    @JsonIgnore
    public double getTotalValue() {
        return this.items
                .stream()
                .mapToDouble((product) -> product.getValue() * product.getQuantity())
                .sum();
    }

    @JsonIgnore
    public Integer getProductQuantity(final Product product) {
        return items.get(items.indexOf(product)).getQuantity();
    }

    @JsonIgnore
    public Set<Product> getProducts() {
        return Set.copyOf(items);
    }
}
