package com.monk.discount.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.monk.discount.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BuyXGetYCouponTest {

    @Test
    void testInvalidRequiredProductCount() {
        assertThrows(IllegalArgumentException.class, () -> new BuyXGetYCoupon(Set.of(1L), 0, Set.of(2L), 1));
    }

    @Test
    void testInvalidFreeProductCount() {
        assertThrows(IllegalArgumentException.class, () -> new BuyXGetYCoupon(Set.of(1L), 10, Set.of(2L), 0));
    }

    @Test
    void testInvalidProductIds() {
        assertThrows(IllegalArgumentException.class, () -> new BuyXGetYCoupon(Set.of(), 10, Set.of(2L), 10));
    }

    @Test
    void testRequiredProductNotInCart() {
        final Coupon coupon = new BuyXGetYCoupon(Set.of(2L), 10, Set.of(1L), 10);
        assertEquals(0.0, coupon.computeDiscount(TRANSACTION_1));
    }

    @Test
    void testFreeProductNotInCart() {
        final Coupon coupon = new BuyXGetYCoupon(Set.of(1L), 10, Set.of(2L), 10);
        assertEquals(0.0, coupon.computeDiscount(TRANSACTION_1));
    }

    @Test
    void testRequiredProductNotInEnoughQty() {
        final Coupon coupon = new BuyXGetYCoupon(Set.of(1L), 100, Set.of(2L), 10);
        assertEquals(0.0, coupon.computeDiscount(TRANSACTION_2));
    }

    @Test
    void testDiscountOnMultipleItems() {
        final Coupon coupon = new BuyXGetYCoupon(Set.of(1L), 1, Set.of(3L, 4L), 10);
        assertEquals(780.0, coupon.computeDiscount(TRANSACTION_3));
    }

    @Test
    void testDiscountOnAllFreeItems() {
        final Coupon coupon = new BuyXGetYCoupon(Set.of(1L), 1, Set.of(3L), 10);
        assertEquals(60.0, coupon.computeDiscount(TRANSACTION_2));
    }
}