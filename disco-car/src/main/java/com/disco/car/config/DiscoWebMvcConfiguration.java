package com.disco.car.config;

import com.disco.car.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @ClassName: DiscoWebMvcConfiguration
 * @Description: 过滤器注册器
 * @date: 2023/7/22
 * @author zhb
 */
@Configuration
public class DiscoWebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * 将登录过滤器注册进mvc过滤器中
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
