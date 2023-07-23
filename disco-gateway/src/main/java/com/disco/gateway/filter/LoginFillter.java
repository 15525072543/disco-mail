package com.disco.gateway.filter;

import com.disco.auth.common.pojo.UserInfo;
import com.disco.auth.common.utils.JwtUtils;
import com.disco.gateway.config.AllowPathConfig;
import com.disco.gateway.config.JwtConfig;
import com.leyou.common.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: LoginFillter
 * @Description:
 * @date: 2023/7/17
 * @author zhb
 */
@EnableConfigurationProperties({JwtConfig.class, AllowPathConfig.class})
@Component
public class LoginFillter extends ZuulFilter{

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private AllowPathConfig allowPathConfig;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String url = request.getRequestURL().toString();
        //只要路由的路径，不包含白名单的路径，就进行拦截器方法，校验用户登录状态
        for (String allowPath : allowPathConfig.getAllowPaths()) {
            if (url.contains(allowPath)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //1.初始化zuul拦截器的上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //2.获取cookie中的token
        HttpServletRequest request = requestContext.getRequest();
        String token = CookieUtils.getCookieValue(request, this.jwtConfig.getCookieName());
        //3.解析token，并查看是否解析成功(jwt超出有效时间、cookie中没有DISCO_TOKEN数据)都会抛出异常
        try {
            JwtUtils.getInfoFromToken(token, this.jwtConfig.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            //不进行路由转发
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
