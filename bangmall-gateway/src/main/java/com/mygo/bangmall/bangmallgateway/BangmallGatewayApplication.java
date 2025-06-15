package com.mygo.bangmall.bangmallgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BangmallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BangmallGatewayApplication.class, args);
    }

}
