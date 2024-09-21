package com.monk.discount;

import com.monk.discount.foreign.Transaction;
import com.monk.discount.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/v1")
public class DiscountController {

    private final DiscountService service;

    @Autowired
    public DiscountController(final DiscountService service) {
        this.service = service;
    }

    @PostMapping("/coupon")
    public <T extends Coupon> ResponseEntity<Long> createCoupon(@RequestBody T coupon) {
        try {
            service.putAll(List.of(coupon));
            return ResponseEntity.ok(coupon.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/coupons")
    public <T extends Coupon> ResponseEntity<List<Long>> createCoupon(@RequestBody List<T> coupons) {
        try {
            service.putAll(List.copyOf(coupons));
            return ResponseEntity.ok(coupons.stream().map(Coupon::getId).toList());
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<Coupon>> get() {
        return ResponseEntity.ok(service.get());
    }

    @GetMapping("/coupon/{id}")
    public ResponseEntity<Coupon> get(@PathVariable Long id) {
        final Optional<Coupon> optionalCoupon = Optional.ofNullable(service.get(id));

        return optionalCoupon
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(BAD_REQUEST).build());
    }

    @DeleteMapping("/coupon/{id}")
    public ResponseEntity<Coupon> delete(@PathVariable Long id) {
        final Optional<Coupon> optionalCoupon = service.delete(id);

        return optionalCoupon
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(BAD_REQUEST).build());
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Map<Long, Double>> evaluateCart(@RequestBody Transaction transaction) {

        final Map<Coupon, Double> result = service.evaluate(transaction);

        final Map<Long, Double> response = new HashMap<>();

        result.forEach((key, value) -> {
            if (value > 0.0)
                response.put(key.getId(), value);
        });

        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply/{id}")
    public ResponseEntity<Double> apply(@RequestBody Transaction transaction, @PathVariable Long id) {

        try {
            return ResponseEntity.ok(service.apply(transaction, id));
        } catch (Exception e) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
    }


}
