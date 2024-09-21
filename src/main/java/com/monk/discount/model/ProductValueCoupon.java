package com.monk.discount.model;

import com.monk.discount.foreign.Cart;
import com.monk.discount.foreign.Product;
import com.monk.discount.foreign.Transaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Getter
public class ProductValueCoupon extends AbstractCoupon {
    private static final CouponType COUPON_TYPE = CouponType.PRODUCT;

    @NonNull
    private final Double discount;

    @NonNull
    private final Long productId;

    public ProductValueCoupon(@NonNull Double discount, @NonNull Long productId) {
        super(COUPON_TYPE);
        if (discount.isInfinite() || (discount < 0.0) || (discount > 1.0)) {
            throw new IllegalArgumentException("Discount should be between 0.0 and 1.0.");
        }
        this.discount = discount;
        this.productId = productId;
    }

    @Override
    public double computeDiscount(final Transaction transaction) {
        final Cart cart = transaction.getCart();
        final Optional<Product> optionalProduct = cart.getItems()
                .stream()
                .filter(key -> key.getId().equals(this.productId))
                .findFirst();

        return optionalProduct
                .map(product -> discount * product.getValue() * cart.getProductQuantity(product))
                .orElse(0.0);

    }
}
