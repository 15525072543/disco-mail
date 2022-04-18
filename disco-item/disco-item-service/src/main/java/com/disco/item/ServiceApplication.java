package com.disco.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: ZHB
 * @Date: 2022/03/09/13:58
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.disco.item.mapper")
public class ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
