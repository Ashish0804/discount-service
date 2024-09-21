package com.monk.discount.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
public abstract class AbstractCoupon implements Coupon{
    @NonNull
    final Long id;

    @NonNull
    final CouponType type;


    public AbstractCoupon(@NonNull final CouponType type) {
        this.type = type;
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }


}
