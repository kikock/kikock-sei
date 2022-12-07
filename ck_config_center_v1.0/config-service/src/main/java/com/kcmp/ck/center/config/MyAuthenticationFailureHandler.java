package com.kcmp.ck.center.config;

import com.kcmp.ck.config.entity.dto.OperateResult;
import com.kcmp.ck.config.entity.enums.OperateStatusEnum;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @project_name: ck_config_center_v1.0
 * @description: 认证失败
 * @create_name: kikock
 * @create_date: 2022-04-14 08:57
 **/
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //设置返回请求头
        response.setContentType("application/json;charset=utf-8");
        //写出流
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        out.write(mapper.writeValueAsString(new OperateResult(OperateStatusEnum.ERROR, "登录失败")));
        out.flush();
        out.close();
    }
}
