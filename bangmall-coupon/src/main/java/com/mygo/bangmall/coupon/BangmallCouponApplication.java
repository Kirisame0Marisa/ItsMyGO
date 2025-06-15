package com.mygo.bangmall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BangmallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(BangmallCouponApplication.class, args);
    }

}
