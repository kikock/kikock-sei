package com.kcmp.ck.center.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @project_name: ck_config_center_v1.0
 * @description: 用户认证
 * @create_name: kikock
 * @create_date: 2022-04-13 17:23
 **/
@Component
@ComponentScan
public class MyAuthenticationProvider implements AuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private MyUserDetailsService myUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //根据输入的用户密码，读取数据库中信息
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        MyUserDetails user = (MyUserDetails) myUserDetailsService.loadUserByUsername(username);

        //判断是否有效用户
        if (!user.isEnabled()) {
            throw new DisabledException("用户禁用");
        } else if (!user.isAccountNonLocked()) {
            throw new LockedException("用户锁定");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("用户过期");
        } else if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("用户已经退出");
        }
        //验证用户密码是否正确
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("用户密码错误!");
        }


        logger.info(String.format("用户%s登录成功", username));

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
