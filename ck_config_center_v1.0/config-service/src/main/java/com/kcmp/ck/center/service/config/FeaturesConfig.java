package com.kcmp.ck.center.service.config;

import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kikock
 * CXF 功能配置
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Configuration
public class FeaturesConfig {
    @Value("${cxf.path}")
    private String basePath;

    @Bean("swagger2Feature")
    public Feature swagger2Feature() {
        Swagger2Feature result = new Swagger2Feature();
        result.setTitle("Config_Center - API文档");
        result.setBasePath(this.basePath);
        result.setContact("kikock@qq.com");
        result.setVersion("v1.0.0");
        result.setSchemes(new String[]{"http", "https"});
        result.setPrettyPrint(true);
        return result;
    }
}
