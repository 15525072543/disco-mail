package com.disco.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: CarApplication
 * @Description: 购物车模块启动器
 * @date: 2023/7/20
 * @author zhb
 */
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class CarApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarApplication.class);
    }
}
