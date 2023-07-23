package com.disco.car.interceptor;

import com.disco.auth.common.pojo.UserInfo;
import com.disco.auth.common.utils.JwtUtils;
import com.disco.car.config.JwtConfig;
import com.leyou.common.utils.CookieUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: LoginInterceptor
 * @Description: 登录拦截器，解析用户信息
 * @date: 2023/7/22
 * @author zhb
 */
@EnableConfigurationProperties(JwtConfig.class)
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Resource
    private JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie中的token
        String token = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());
        //解析token，获取用户信息
        UserInfo user = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
        if (user == null){
            return false;
        }
        //将用户信息存入 THREAD_LOCAL 中
        THREAD_LOCAL.set(user);
        return true;
    }

    /**
     * 获取THREAD_LOCAL中的user对象
     * @return
     */
    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空线程的局部变量。因为使用的是tomcat线程池，线程不会结束，也就不会释放线程的局部变量。
        THREAD_LOCAL.remove();
    }
}
