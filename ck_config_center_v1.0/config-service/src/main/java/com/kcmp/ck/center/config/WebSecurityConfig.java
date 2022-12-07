package com.kcmp.ck.center.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by kikock
 * web安全配置
 *
 * @author kikock
 * @email kikock@qq.com
 **/
@Configuration
@EnableWebSecurity
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationProvider myAuthenticationProvider;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();


        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.csrf().disable()
                .authorizeRequests();

        //不需要保护的资源路径允许访问
        registry
                .antMatchers("/js/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/ajax/**").permitAll()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/base/**").permitAll()
                .antMatchers("/web/login").permitAll()
                .antMatchers("/web/logout").permitAll()
                .anyRequest().authenticated()
                //登录
                .and()
                .formLogin()
                //默认是/login，不用改
                .loginPage("/web/login")
                //登录成功
                .successHandler(myAuthenticationSuccessHandler)
                //登录失败
                .failureHandler(myAuthenticationFailureHandler)
                .permitAll()
                .and()
                //退出
                .logout()
                //默认为/logout，不用改
                .logoutUrl("/web/logout")
                .logoutSuccessUrl("/web/login") //默认跳转到 /login
                .deleteCookies("JSESSIONID")  //默认也是会删除cookie的
                .permitAll();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(myAuthenticationProvider);
    }

}

