package com.disco.auth.service.config;

import com.disco.auth.common.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @ClassName: JwtConfig
 * @Description: 从配置文件读取的jwt信息
 * @date: 2023/7/15
 * @author zhb
 */
@ConfigurationProperties(prefix = "disco.jwt")
public class JwtConfig {
    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private Integer expire;
    private String cookieName;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * 初始化 publicKey（公钥对象） privateKey（私钥对象）
     */
    @PostConstruct
    private void init(){
        File pubKey = new File(this.pubKeyPath);
        File priKey = new File(this.priKeyPath);
        try {
            if (!pubKey.exists() || !priKey.exists()){
                // 加盐生成公钥文件和私钥文件
                RsaUtils.generateKey(this.pubKeyPath,priKeyPath,this.secret);
            }
            // 获取秘钥文件中的秘钥内容，并转化为秘钥对象
            this.publicKey = RsaUtils.getPublicKey(this.pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(this.priKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
