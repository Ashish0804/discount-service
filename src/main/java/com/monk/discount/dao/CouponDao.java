package com.monk.discount.dao;

import com.monk.discount.model.Coupon;

import java.util.List;

public interface CouponDao {

    Coupon get(Long id);

    List<Coupon> getAll();

    void upsert(Coupon coupon);

    void delete(Long id);

}
