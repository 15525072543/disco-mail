package com.disco.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName: AllowPathConfig
 * @Description:
 * @date: 2023/7/17
 * @author zhb
 */
@ConfigurationProperties(prefix = "disco.filter")
public class AllowPathConfig {

    private List<String> allowPaths;

    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }
}
