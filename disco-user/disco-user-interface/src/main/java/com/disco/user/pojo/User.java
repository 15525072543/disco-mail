package com.disco.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @ClassName: User
 * @Description: 用户实体类
 * @date: 2023/7/13
 * @author zhb
 */
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //用户id

    @Length(min = 4,max = 30,message = "用户名必须在4-30位中间")
    private String username; //用户名

    @JsonIgnore
    @Length(min = 4,max = 30,message = "密码必须在4-30位中间")
    private String password; //密码

    @Pattern(regexp = "^1[356789]\\d{9}$",message = "手机号格式不正确")
    private String phone; //手机号

    private Date created; //创建时间

    @JsonIgnore
    private String salt; //密码的盐值

    public User() {
    }

    public User(Long id, String username, String password, String phone, Date created, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.created = created;
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", phone='" + phone + '\'' +
            ", created=" + created +
            ", salt='" + salt + '\'' +
            '}';
    }
}
