package com.kcmp.test.ck;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by kikock
 *
 * @email kikock@qq.com
 * @create 2019-12-20 16:53
 **/
public class TestServiceServlet extends SpringBootServletInitializer {
    /**
     * Configure the application. Normally all you would need to do is to add sources
     * (e.g. config classes) because other settings have sensible defaults. You might
     * choose (for instance) to add default command line arguments, or set an active
     * Spring profile.
     *
     * @param builder a builder for the application context
     * @return the application builder
     * @see SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TestServiceApplication.class);
    }
}
