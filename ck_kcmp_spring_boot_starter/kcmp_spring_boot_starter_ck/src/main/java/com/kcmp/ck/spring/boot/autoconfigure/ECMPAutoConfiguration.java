package com.kcmp.ck.spring.boot.autoconfigure;

import com.kcmp.ck.context.BaseApplicationContext;
import com.kcmp.ck.eventcenter.EventCenter;
import com.kcmp.ck.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

/**
 * Created by kikock
 * 平台配置
 * @email kikock@qq.com
 **/
public class ECMPAutoConfiguration {

    @Bean
    public BaseApplicationContext ecmpContext() {
        return new BaseApplicationContext();
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/lang/ecmp-lang", "classpath:/lang/messages");
        messageSource.setCacheSeconds(120);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(Environment env) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        //JWT签名密钥
        String secret = env.getProperty("com.kcmp.ck.security.jwt.secret");
        if (StringUtils.isNotBlank(secret)) {
            jwtTokenUtil.setJwtSecret(secret);
        }
        //会话超时时间。
        int sessionTimeout = env.getProperty("server.servlet.session.timeout", Integer.class, 3600);
        //JWT过期时间（秒）
        jwtTokenUtil.setJwtExpiration(sessionTimeout + 300);
        return jwtTokenUtil;
    }

    @Bean
    public EventCenter eventCenter() {
        return new EventCenter();
    }

}
