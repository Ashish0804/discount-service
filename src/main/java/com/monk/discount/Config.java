package com.monk.discount;

import com.monk.discount.dao.CouponDao;
import com.monk.discount.dao.LocalCouponDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    CouponDao getDao() {
        return new LocalCouponDao();
    }
}
