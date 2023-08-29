package com.macro.mall.portal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "basic")
public class BasicConfiguration {
    /**
     * 文件服务器连接
     */
    private String fileServerUrl;
}