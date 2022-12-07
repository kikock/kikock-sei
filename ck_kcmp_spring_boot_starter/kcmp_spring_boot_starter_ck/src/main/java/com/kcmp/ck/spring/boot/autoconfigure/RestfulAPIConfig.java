package com.kcmp.ck.spring.boot.autoconfigure;

import com.kcmp.ck.context.ConfigConstants;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.util.*;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.spring.SpringResourceFactory;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.jws.WebService;
import javax.ws.rs.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kikock
 * restful 接口配置类
 * @email kikock@qq.com
 **/
@Configuration
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
@ConditionalOnProperty(name = "ServletContainerEnvironment", havingValue = "true")
@ConditionalOnWebApplication
public class RestfulAPIConfig implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestfulAPIConfig.class);

    @Autowired
    private ApplicationContext context;

    @Autowired(required = false)
    private ZipkinTracingContainerFilter tracingContainerFilter;

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() {
        EndpointImpl endpoint;
        WebService webService;
        SpringBus bus = springBus();
//        WebServiceLogInterceptor logInterceptor = new WebServiceLogInterceptor();
        for (String beanName : context.getBeanDefinitionNames()) {
            webService = context.findAnnotationOnBean(beanName, WebService.class);
            if (webService != null) {
                endpoint = new EndpointImpl(bus, context.getBean(beanName));
                endpoint.publish(webService.serviceName());
                //添加拦截器
                // 通过注解代替 @InInterceptors(classes = {WebServiceLogInterceptor.class})
//                endpoint.getInInterceptors().add(logInterceptor);
            }
        }
    }

    @Bean
    public ServletRegistrationBean cxfServlet() {
        ServletRegistrationBean<CXFServlet> registration = new ServletRegistrationBean<>(new CXFServlet());
        registration.setName("cxfServlet");
        registration.addUrlMappings("/*");
        return registration;
    }

    @Bean
    public Swagger2Feature swagger2Feature() {
        Swagger2Feature swagger2Feature = new Swagger2Feature();
        swagger2Feature.setBasePath("/");
        swagger2Feature.setContact("kikock@qq.com");
        swagger2Feature.setResourcePackage(ContextUtil.getGlobalProperty(ConfigConstants.PARAM_API_PACKAGE));
        //swagger2Feature.setScan(true);
        swagger2Feature.setTitle(ContextUtil.getAppCode() + " - API文档");
        swagger2Feature.setDescription(swagger2Feature.getTitle());

        return swagger2Feature;
    }

    @Bean(Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Server jaxRsServer(ApplicationContext ctx) {
        List<Object> providers = new LinkedList<Object>();
        providers.add(new ApiRestJsonProvider());
        providers.add(new TokenCheckRequestFilter());
        providers.add(new TokenCleanResponseFilter());
        providers.add(new InvokeFaultExceptionMapper());
//        providers.add(new SessionContainerRequestFilter());
//        providers.add(new ApiServiceContainerResponseFilter());
//         zipkin filter
        if (Objects.nonNull(tracingContainerFilter)) {
            providers.add(tracingContainerFilter);
        }

        LinkedList<ResourceProvider> resourceProviders = new LinkedList<>();
        for (String beanName : ctx.getBeanDefinitionNames()) {
            if (ctx.findAnnotationOnBean(beanName, Path.class) != null) {
                SpringResourceFactory factory = new SpringResourceFactory(beanName);
                factory.setApplicationContext(ctx);
                resourceProviders.add(factory);
            }
        }
        if (resourceProviders.size() == 0) {
            LOGGER.error("未找到API接口实现，请仔细检查api包下的接口定义。");
            System.exit(0);
        }

        List<Feature> features = new LinkedList<Feature>();
        features.add(swagger2Feature());

        //拦截器
//        List<Interceptor<? extends Message>> interceptorsIn = new ArrayList<Interceptor<? extends Message>>();
//        interceptorsIn.add(tokenCheckInterceptor());

        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(springBus());
        factory.setProviders(providers);
        factory.setResourceProviders(resourceProviders);
        factory.setFeatures(features);
//        factory.setInInterceptors(interceptorsIn);
        return factory.create();
    }

    //禁用Spring Boot默认的ErrorPageFilter
    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        //禁用
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }
}
