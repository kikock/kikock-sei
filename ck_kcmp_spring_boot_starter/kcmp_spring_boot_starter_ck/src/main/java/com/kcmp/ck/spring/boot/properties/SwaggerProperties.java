package com.kcmp.ck.spring.boot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by kikock
 * 接口文档配置
 * @email kikock@qq.com
 **/
@ConfigurationProperties("swagger")
public class SwaggerProperties {
    /**
     * 是否启用
     */
    private boolean enable = true;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 版本
     */
    private String version;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
