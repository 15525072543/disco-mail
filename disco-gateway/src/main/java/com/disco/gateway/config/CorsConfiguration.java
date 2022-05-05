package com.disco.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName: CorsConfiguration
 * @Description: 使用cors处理跨域问题
 * @date: 2022/4/21
 * @author zhb
 */
@Configuration
public class CorsConfiguration {

    @Bean
    public CorsFilter corsFilter(){
        //初始化cors配置对象
        org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
        //允许跨域的域名,如果要携带cookie,不能写* *:代表所有域名都可以跨域访问
        corsConfiguration.setAllowCredentials(true); //允许携带cookie
        corsConfiguration.addAllowedMethod("*"); //代表所有请求方法 GET, PUT, POST, DELETE.....
        corsConfiguration.addAllowedHeader("*"); //允许携带任何头信息

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",corsConfiguration);

        //返回corsFilter实例, 参数:cors配置源对象
        return new CorsFilter(configurationSource);
    }
}
