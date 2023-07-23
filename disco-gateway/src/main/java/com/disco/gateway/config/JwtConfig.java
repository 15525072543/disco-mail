package com.disco.gateway.config;

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
    private String pubKeyPath;
    private String cookieName;
    private PublicKey publicKey;


    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

  public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }


    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }


    /**
     * 初始化 publicKey（公钥对象） privateKey（私钥对象）
     */
    @PostConstruct
    private void init(){
        try{
            // 获取秘钥文件中的秘钥内容，并转化为秘钥对象
            this.publicKey = RsaUtils.getPublicKey(this.pubKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
