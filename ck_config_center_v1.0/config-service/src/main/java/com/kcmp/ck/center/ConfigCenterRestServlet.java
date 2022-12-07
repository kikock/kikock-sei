package com.kcmp.ck.center;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by kikock
 * SpringBoot War Servlet
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class ConfigCenterRestServlet extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ConfigCenterRestApplication.class);
    }
}
