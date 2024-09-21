package com.monk.discount.foreign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
public class Transaction {
    @NonNull
    private final Cart cart;

    // TODO: add User field
    // TODO: add Payment Type field
    // TODO: add date field
}
