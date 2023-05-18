package com.kcmp.ck.spring.boot.autoconfigure;

//import com.kcmp.ck.context.ContextUtil;
//import com.kcmp.ck.spring.boot.properties.SwaggerProperties;
//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
//import io.swagger.annotations.Api;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by kikock
 * 自动生成API文档配置
 * @email kikock@qq.com
 **/
//@Configuration
//@EnableKnife4j
//@EnableSwagger2
//@Import(BeanValidatorPluginsConfiguration.class)
//@EnableConfigurationProperties({SwaggerProperties.class})
//@ConditionalOnProperty(name = "enable", prefix = "swagger", havingValue = "true", matchIfMissing = true)
public class SwaggerConfiguration {

//    private ApiInfo apiInfo(SwaggerProperties swagger) {
//        return new ApiInfoBuilder()
//                .title(ContextUtil.getAppCode() + " - API文档")
//                .description(ContextUtil.getAppCode() + " - API文档")
//                .version(StringUtils.isBlank(swagger.getVersion()) ? ContextUtil.getProperty("application.version") : swagger.getVersion())
//                .build();
//    }
//
//    @Bean
//    public Docket api(SwaggerProperties swaggerProperties) {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo(swaggerProperties));
//    }
}
