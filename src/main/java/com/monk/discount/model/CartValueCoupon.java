package com.monk.discount.model;

import com.monk.discount.foreign.Cart;
import com.monk.discount.foreign.Transaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = true)
@Getter
public class CartValueCoupon extends AbstractCoupon {
    private static final CouponType COUPON_TYPE = CouponType.CART_VALUE;

    @NonNull private final Double discount;
    @NonNull private final Double minCartValue;

    public CartValueCoupon(@NonNull final Double discount, @NonNull final Double minCartValue) {
        super(COUPON_TYPE);

        if (discount.isInfinite() || (discount < 0.0) || (discount > 1.0)) {
            throw new IllegalArgumentException("Discount should be between 0.0 and 1.0.");
        }

        if (minCartValue.isInfinite() || (minCartValue < 0.0)) {
            throw new IllegalArgumentException("Invalid minCartValue.");
        }

        this.discount = discount;
        this.minCartValue = minCartValue;
    }

    @Override
    public double computeDiscount(final Transaction transaction) {
        final Cart cart = transaction.getCart();
        return cart.getTotalValue() >= minCartValue ?
                cart.getTotalValue() * discount :
                0.0;
    }
}
