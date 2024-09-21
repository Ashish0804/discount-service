package com.monk.discount.dao;

import com.monk.discount.model.BuyXGetYCoupon;
import com.monk.discount.model.CartValueCoupon;
import com.monk.discount.model.Coupon;
import com.monk.discount.model.ProductValueCoupon;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LocalCouponDao implements CouponDao {


    final HashMap<Long, Coupon> table = new HashMap<>();

    public LocalCouponDao() {
        final CartValueCoupon cartValueCoupon = new CartValueCoupon(0.5, 1000.0);
        final ProductValueCoupon productValueCoupon = new ProductValueCoupon(0.5, 1L);
        final BuyXGetYCoupon buyXGetYCoupon = new BuyXGetYCoupon(Set.of(1L, 2L), 2, Set.of(3L, 4L), 1);


        table.put(cartValueCoupon.getId(), cartValueCoupon);
        table.put(productValueCoupon.getId(), productValueCoupon);
        table.put(buyXGetYCoupon.getId(), buyXGetYCoupon);
    }


    @Override
    public Coupon get(Long id) {
        return table.get(id);
    }

    @Override
    public List<Coupon> getAll() {
        return table.values().stream().toList();
    }

    @Override
    public void upsert(Coupon coupon) {
        table.put(coupon.getId(), coupon);

    }

    @Override
    public void delete(Long id) {
        table.remove(id);

    }
}
