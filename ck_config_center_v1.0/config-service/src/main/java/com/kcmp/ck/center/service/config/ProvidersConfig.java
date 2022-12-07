package com.kcmp.ck.center.service.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ext.Provider;

/**
 * Created by kikock
 * CXF 驱动配置
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Provider
@Configuration
public class ProvidersConfig {
    @Bean
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJsonProvider();
    }
}
