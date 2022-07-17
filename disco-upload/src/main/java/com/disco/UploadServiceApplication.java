package com.disco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName: UploadServiceApplication
 * @Description: 上传服务启动类
 * @date: 2022/6/1
 * @author zhb
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UploadServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadServiceApplication.class);
    }
}
