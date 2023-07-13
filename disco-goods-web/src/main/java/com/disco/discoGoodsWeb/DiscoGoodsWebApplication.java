package com.disco.discoGoodsWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: DiscoGoodsWebApplication
 * @Description: 微服务启动类
 * @date: 2023/7/7
 * @author zhb
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class DiscoGoodsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoGoodsWebApplication.class);
    }
}
