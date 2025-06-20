package com.mygo.bangmall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BangmallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(BangmallWareApplication.class, args);
    }

}
