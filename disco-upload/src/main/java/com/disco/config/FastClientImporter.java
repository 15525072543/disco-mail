package com.disco.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @ClassName: FastClientImporter
 * @Description: 配置FastDFS 客户端
 * @date: 2022/6/26
 * @author zhb
 */
@Configuration
@Import(FdfsClientConfig.class)
// 解决jmx冲负注册bean的问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastClientImporter {
}
