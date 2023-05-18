package com.kcmp.ck;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by kikock
 * 使用自定义容器运行war包
 * @author kikock
 * @email kikock@qq.com
 **/
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FlowApplication.class);
    }

}
