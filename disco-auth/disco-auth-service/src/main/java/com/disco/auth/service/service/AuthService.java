package com.disco.auth.service.service;

import com.disco.auth.common.pojo.UserInfo;
import com.disco.auth.common.utils.JwtUtils;
import com.disco.auth.service.client.UserClient;
import com.disco.auth.service.config.JwtConfig;
import com.disco.user.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName: AuthService
 * @Description:
 * @date: 2023/7/15
 * @author zhb
 */
@Service
public class AuthService {

    @Resource
    private UserClient userClient;

    @Resource
    private JwtConfig jwtConfig;

    public String accredit(String username, String password) {
        //1.调用用户模块，根据用户名和密码查询用户信息
        User user = this.userClient.queryUser(username, password);
        //2.判断是否有用户信息，如果没有，返回null，授权失败
        if (user == null){
            return null;
        }
        //3.有用户信息，使用jwt加密用户信息，生成token
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            return JwtUtils.generateToken(userInfo,this.jwtConfig.getPrivateKey(),30);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
