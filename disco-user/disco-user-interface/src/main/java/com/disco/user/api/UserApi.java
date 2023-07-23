package com.disco.user.api;

import com.disco.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: UserApi
 * @Description:
 * @date: 2023/7/15
 * @author zhb
 */
public interface UserApi {

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public User queryUser (@RequestParam("username") String username,
    @RequestParam("password") String password);
}
