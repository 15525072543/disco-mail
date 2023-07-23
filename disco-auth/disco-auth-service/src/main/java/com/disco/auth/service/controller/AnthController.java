package com.disco.auth.service.controller;

import com.disco.auth.common.pojo.UserInfo;
import com.disco.auth.common.utils.JwtUtils;
import com.disco.auth.service.config.JwtConfig;
import com.disco.auth.service.service.AuthService;
import com.leyou.common.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: AnthController
 * @Description:
 * @date: 2023/7/15
 * @author zhb
 */
@Controller
@EnableConfigurationProperties(JwtConfig.class)
public class AnthController {

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private AuthService authService;

    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        HttpServletRequest request,
        HttpServletResponse response){

        //1.调用服务层授权方法获取用户token值
        String token = this.authService.accredit(username,password);
        if (StringUtils.isBlank(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //2.将token值存入cookie中
        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),token, jwtConfig.getExpire() * 60);
        return ResponseEntity.ok(null);
    }

    /**
     * 判断用户是否登录
     * @param token cookie中的token值
     * @param request
     * @param response
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
        @CookieValue("DISCO_TOKEN") String token,
        HttpServletRequest request,
        HttpServletResponse response){
        try {
            //1.使用jwt工具解析token
            UserInfo user = JwtUtils.getInfoFromToken(token, this.jwtConfig.getPublicKey());
            //2.判断token是否解析成功(cookie中是否有DISCO_TOKEN)
            if (user == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //3.更新cookie的有效时间
            CookieUtils.setCookie(request,response,"DISCO_TOKEN",token,this.jwtConfig.getExpire() * 60);
            //4.更新jwt的有效时间
            JwtUtils.generateToken(user,this.jwtConfig.getPrivateKey(),this.jwtConfig.getExpire());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
