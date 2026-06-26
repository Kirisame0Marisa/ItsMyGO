package com.mygo.bangmall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "bangmall.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
    private Integer corePoolSize;
    private Integer maxPoolSize;
    //private Integer queueCapacity;
    private Integer keepAliveTime;
}
