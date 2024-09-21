package com.monk.discount.model;

import org.junit.jupiter.api.Test;

import static com.monk.discount.TestUtils.TRANSACTION_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartValueCouponTest {

    @Test
    void testInvalidDiscount() {
        assertThrows(IllegalArgumentException.class, () -> new CartValueCoupon(1.1, 10.0));
    }

    @Test
    void testInvalidCartValue() {
        assertThrows(IllegalArgumentException.class, () -> new CartValueCoupon(0.1, -1.0));
    }

    @Test
    void testCouponNotApplicable() {
        final Coupon coupon = new CartValueCoupon(0.1, 100.0);

        assertEquals(0.0, coupon.computeDiscount(TRANSACTION_1));
    }

    @Test
    void testCouponApplicable() {
        final Coupon coupon = new CartValueCoupon(0.5, 10.0);

        assertEquals(10.0, coupon.computeDiscount(TRANSACTION_1));
    }

}