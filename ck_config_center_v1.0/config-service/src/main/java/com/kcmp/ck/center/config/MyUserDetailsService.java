package com.kcmp.ck.center.config;

import com.kcmp.ck.context.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kikock
 * 用户数据处理，这里是和数据库交互的关键点
 *
 * @author kikock
 * @email kikock@163.com
 **/
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO 配置中心默认一个用户admin 密码admin123
        //数据库逻辑 查询用户,密码,权限 进行用户验证
        String user = ContextUtil.getGlobalProperty("center.uesr");
        String password = ContextUtil.getGlobalProperty("center.password");
        if (StringUtils.isNoneBlank(username) || user.equals(username.toLowerCase())) {
            MyUserDetails userList = new MyUserDetails();
            List<Authority> authorities = new ArrayList<>();
            Authority authority = new Authority("ADMIN");
            authorities.add(authority);
            userList.setUsername(user);
            userList.setPassword(password);
            userList.setAuthorities(authorities);
            return userList;
        }
        throw new UsernameNotFoundException("用户不存在！");
    }


}
