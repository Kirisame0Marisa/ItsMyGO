package com.mygo.bangmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "com.mygo.bangmall.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class BangmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(BangmallProductApplication.class, args);
    }

}
