package com.kcmp.ck.center.service.config;

import com.kcmp.ck.context.BaseApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by kikock
 * 动态加载外部配置文件(Ordered)
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class DynamicEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final Logger log = LoggerFactory.getLogger(DynamicEnvironmentPostProcessor.class);

    /**
     * Post-process the given {@code environment}.
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String filePath = System.getProperty("catalina.base") + "/webapps/config-center-service.properties";
        File configFile = new File(filePath);
        if (!configFile.exists()) {
            filePath = "/usr/local/tomcat/webapps/application.properties";
        }
        try (InputStream inputStream = new FileInputStream(filePath)) {
            Properties source = new Properties();
            source.load(inputStream);
            //注册自己的自动注册
            application.addListeners(new ApplicationReadyEventListener());
            //关闭平台的自动注册
            source.put("isLocalConfig", true);
            //开启rpc
            source.put("spring.grpc.enable", true);
            PropertiesPropertySource propertySource = new PropertiesPropertySource("config-center-service", source);
            environment.getPropertySources().addLast(propertySource);

            BaseApplicationContext baseContext = new BaseApplicationContext();
            baseContext.setEnvironment(environment);

            log.info("load properties from：{}", filePath);
            System.out.println("load properties from：" + filePath);
        } catch (IOException e) {
            log.error("load properties error！", e);
            System.out.println("load properties error!" + e.getMessage());
            log.info("load properties from：application.properties");
        }
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return ConfigFileApplicationListener.DEFAULT_ORDER - 1;
    }
}
