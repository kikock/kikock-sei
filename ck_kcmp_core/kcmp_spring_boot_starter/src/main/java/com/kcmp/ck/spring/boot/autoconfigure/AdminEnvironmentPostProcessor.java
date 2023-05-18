package com.kcmp.ck.spring.boot.autoconfigure;

import com.kcmp.ck.context.ContextUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kikock
 * 管理环境后处理程序
 * @email kikock@qq.com
 **/
@SuppressWarnings("unchecked")
public class AdminEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final Pattern pattern = Pattern.compile("http[s]?://([\\w-]+\\.)+[\\w-]+(:\\d*)?");
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties properties = new Properties();

        //springboot Admin 开关。本地默认关闭
        if (!BooleanUtils.toBoolean(environment.getProperty("isLocalConfig", "true"))
                && BooleanUtils.toBoolean(environment.getProperty("ServletContainerEnvironment", "false"))) {
            String adminServerUrl = ContextUtil.getAdminUrl();
            if (StringUtils.isNotBlank(adminServerUrl)) {
                properties.setProperty("spring.boot.admin.client.enabled", "true");

                //admin server 的地址列表，此设置会触发自动配置，必须
                properties.setProperty("spring.boot.admin.client.url", adminServerUrl);
                //公开所有的端点
                properties.setProperty("management.endpoints.web.exposure.include", "*");
                String baseUrl = ContextUtil.getAppBaseUrl();
                if (StringUtils.isNotBlank(baseUrl)) {
                    properties.setProperty("spring.boot.admin.client.instance.health-url", baseUrl + "/monitor/health");

                    Matcher matcher = pattern.matcher(baseUrl);
                    while (matcher.find()) {
                        baseUrl = matcher.group(0);
                    }
                    properties.setProperty("spring.boot.admin.client.instance.service-base-url", baseUrl);
                }
            } else {
                properties.setProperty("spring.boot.admin.client.enabled", "false");
            }
        } else {
            properties.setProperty("spring.boot.admin.client.enabled", "false");
        }

        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("KCMP-Admin-Config", properties);
        environment.getPropertySources().addFirst(propertiesPropertySource);
    }

}
