package com.monk.discount.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monk.discount.foreign.Transaction;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductValueCoupon.class, name = "PRODUCT"),
        @JsonSubTypes.Type(value = CartValueCoupon.class, name = "CART_VALUE"),
        @JsonSubTypes.Type(value = BuyXGetYCoupon.class, name = "BUY_X_GET_Y")
})
public interface Coupon {
    CouponType getType();
    Long getId();
    double computeDiscount(Transaction transaction);
}
