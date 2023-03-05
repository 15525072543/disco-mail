package com.disco.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: SearchServiceApplication
 * @Description: 启动类
 * @date: 2023/2/5
 * @author zhb
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SearchServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApplication.class);
    }
}
