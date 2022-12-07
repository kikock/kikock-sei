package com.demo.ck;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by kikock
 * SpringBoot War Servlet
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class TestRestServlet extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TestApplication.class);
    }
}
