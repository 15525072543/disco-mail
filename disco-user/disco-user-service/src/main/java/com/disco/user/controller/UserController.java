package com.disco.user.controller;

import com.disco.user.pojo.User;
import com.disco.user.service.UserService;
import org.hibernate.validator.constraints.Length;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @ClassName: UserController
 * @Description:
 * @date: 2023/7/13
 * @author zhb
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 检查电话或者用户名是否唯一
     * @param data 要检测的数据
     * @param type 数据类型 1:用户名 2:手机
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data,@PathVariable("type")Long type){
        Boolean boo = userService.checkUser(data,type);
        if (boo == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boo);
    }

    /**
     * 生成验证码并短信发送
     * @param phone 手机号
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone){
        this.userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 用户注册
     * @param user 用户对象
     * @param code 验证码
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){
        this.userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(
        @RequestParam("username") String username,
        @RequestParam("password") String password){
        User user = this.userService.queryUser(username,password);
        if (user == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
