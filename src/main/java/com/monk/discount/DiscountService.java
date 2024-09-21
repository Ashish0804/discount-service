package com.monk.discount;

import com.monk.discount.dao.CouponDao;
import com.monk.discount.foreign.Transaction;
import com.monk.discount.model.Coupon;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DiscountService {

    private final CouponDao couponDao;

    @Autowired
    public DiscountService(final CouponDao couponDao) {
        this.couponDao = couponDao;
    }


    public <T extends Coupon> void putAll(@NonNull final List<T> coupons) {
        coupons.forEach(couponDao::upsert);
    }

    public Coupon get(final Long id) {
        return couponDao.get(id);
    }

    public List<Coupon> get() {
        return couponDao.getAll();
    }

    public Optional<Coupon> delete(final Long id) {
        final Optional<Coupon> optionalCoupon = Optional.ofNullable(couponDao.get(id));

        if (optionalCoupon.isPresent()) {
            couponDao.delete(id);
        }

        return optionalCoupon;
    }

    public Map<Coupon, Double> evaluate(final Transaction transaction) {
        final Map<Coupon, Double> result = new HashMap<>();

        couponDao.getAll().forEach(coupon -> {
            result.put(coupon, coupon.computeDiscount(transaction));
                }
        );

        return result;
    }

    public Double apply(final Transaction transaction, final Long id) {
        final Optional<Coupon> optionalCoupon = Optional.ofNullable(couponDao.get(id));

        if (optionalCoupon.isEmpty()) {
            throw new IllegalArgumentException("Coupon doesn't exist");
        }

        final double discount = optionalCoupon.get().computeDiscount(transaction);

        if (discount > 0.0) return discount;

        throw new IllegalArgumentException("Coupon is not applicable");
    }




}
