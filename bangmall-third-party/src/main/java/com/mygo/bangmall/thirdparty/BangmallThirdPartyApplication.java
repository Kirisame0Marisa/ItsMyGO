package com.mygo.bangmall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BangmallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BangmallThirdPartyApplication.class, args);
    }

}
