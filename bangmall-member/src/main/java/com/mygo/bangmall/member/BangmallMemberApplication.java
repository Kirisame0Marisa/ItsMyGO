package com.mygo.bangmall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.mygo.bangmall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class BangmallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(BangmallMemberApplication.class, args);
    }

}
