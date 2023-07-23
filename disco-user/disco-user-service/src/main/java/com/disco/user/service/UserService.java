package com.disco.user.service;

import com.disco.user.mapper.UserMapper;
import com.disco.user.pojo.User;
import com.disco.user.utils.CodecUtils;
import com.leyou.common.utils.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: UserService
 * @Description:
 * @date: 2023/7/13
 * @author zhb
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AmqpTemplate amqpTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String key_prefix = "user:verify:";

    public Boolean checkUser(String data,Long type){
        User record = new User();
        if (type == 1){
            record.setUsername(data);
        }else if (type == 2){
            record.setPhone(data);
        }else {
            return null;
        }
        return userMapper.selectCount(record) == 0;
    }

    public void sendVerifyCode(String phone) {
        if (StringUtils.isBlank(phone)){
            return;
        }
        //生成6位数字验证码
        String code = NumberUtils.generateCode(6);
        //发送到消息队列中
        Map<String,String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        this.amqpTemplate.convertAndSend("leyou.sms.exchange","verifycode.sms",msg);
        //存储到redis中
        this.stringRedisTemplate.opsForValue().set(key_prefix + phone,code,5, TimeUnit.MINUTES);
    }

    public void register(User user, String code) {
        //从redis中获取验证码
        String redisKey = key_prefix + user.getPhone();
        String redisCode = this.stringRedisTemplate.opsForValue().get(redisKey);
        //1.比较redis中的验证码和用户输入的验证码
        if (!StringUtils.equals(redisCode,code)){
            return;
        }
        //2.生成salt
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //3.将密码加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        //4.新增用户数据
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);
        //5.删除redis中的验证码缓存
        this.stringRedisTemplate.delete(redisKey);
    }

    public User queryUser(String username, String password) {
        //1.根据用户名查询用户
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (user == null){
            return null;
        }
        //2.将用户输入的密码加盐加密
        password = CodecUtils.md5Hex(password, user.getSalt());
        //3.将加密后的密码和用户注册的密码比较
        if (StringUtils.equals(password,user.getPassword())){
            return user;
        }
        return null;
    }
}
