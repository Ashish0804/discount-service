package com.monk.discount.model;

import com.monk.discount.foreign.Cart;
import com.monk.discount.foreign.Product;
import com.monk.discount.foreign.Transaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BuyXGetYCoupon extends AbstractCoupon {
    private static final CouponType COUPON_TYPE = CouponType.BUY_X_GET_Y;

    @NonNull
    private final Set<Long> requiredProductIds;

    @NonNull
    private final Integer requiredProducts;

    @NonNull
    private final Set<Long> freeProductIds;

    @NonNull
    private final Integer freeProducts;


    public BuyXGetYCoupon(@NonNull final Set<Long> requiredProductIds,
                          @NonNull final Integer requiredProducts,
                          @NonNull final Set<Long> freeProductIds,
                          @NonNull final Integer freeProducts) {
        super(COUPON_TYPE);

        if (!(requiredProducts > 0)) {
            throw new IllegalArgumentException("Invalid required products count.");
        }

        if (!(freeProducts > 0)) {
            throw new IllegalArgumentException("Invalid free products count.");
        }

        if (requiredProductIds.isEmpty() || freeProductIds.isEmpty()) {
            throw new IllegalArgumentException("Empty product ids.");
        }

        this.requiredProductIds = requiredProductIds;
        this.requiredProducts = requiredProducts;
        this.freeProductIds = freeProductIds;
        this.freeProducts = freeProducts;
    }

    @Override
    public double computeDiscount(final Transaction transaction) {
        final Cart cart = transaction.getCart();
        final List<Product> presentRequiredProducts = cart.getProducts().stream()
                .filter(product -> requiredProductIds.contains(product.getId()))
                .toList();

        if (presentRequiredProducts.isEmpty()) return  0.0;

        final LinkedList<Product> presentFreeProducts = new LinkedList<>(cart.getProducts().stream()
                .filter(product -> freeProductIds.contains(product.getId()))
                .sorted(Comparator.comparing(Product::getValue))
                .toList());

        if (presentFreeProducts.isEmpty()) return  0.0;

        final int totalRequiredProductCount = presentRequiredProducts.stream()
                .mapToInt(cart::getProductQuantity)
                .sum();

        if (totalRequiredProductCount < requiredProducts) return  0.0;

        int possibleFreeProducts = freeProducts * (totalRequiredProductCount / requiredProducts);

        final int totalFreeProductCount = presentFreeProducts.stream()
                .mapToInt(cart::getProductQuantity)
                .sum();

        if (possibleFreeProducts >= totalFreeProductCount) {
            return presentFreeProducts.stream()
                    .mapToDouble(product -> cart.getProductQuantity(product) * product.getValue())
                    .sum();
        }

        double totalDiscount = 0.0;

        while (possibleFreeProducts > 0) {
            final Product freeProduct = presentFreeProducts.removeFirst();
            final int productQuantity = cart.getProductQuantity(freeProduct);

            if (productQuantity < possibleFreeProducts) {
                totalDiscount += productQuantity * freeProduct.getValue();
            } else {
                totalDiscount += possibleFreeProducts * freeProduct.getValue();
            }

            possibleFreeProducts -= productQuantity;
        }

        return totalDiscount;

    }
}
