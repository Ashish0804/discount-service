package com.monk.discount.model;

import org.junit.jupiter.api.Test;

import static com.monk.discount.TestUtils.TRANSACTION_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValueCouponTest {


    @Test
    void testInvalidDiscount() {
        assertThrows(IllegalArgumentException.class, () -> new ProductValueCoupon(1.1, 1L));
    }

    @Test
    void testCouponNotApplicable() {
        final Coupon coupon = new ProductValueCoupon(0.1, 19L);

        assertEquals(0.0, coupon.computeDiscount(TRANSACTION_1));
    }

    @Test
    void testCouponApplicable() {
        final Coupon coupon = new ProductValueCoupon(0.5, 1L);

        assertEquals(10.0, coupon.computeDiscount(TRANSACTION_1));
    }
}