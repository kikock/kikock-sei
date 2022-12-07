package com.kcmp.ck.center.controller;


import com.kcmp.ck.config.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * 首页 业务处理
 *
 * @author kikock
 */
@Controller
@RequestMapping("/web")
public class HomeController {


    // 登录页面
    @GetMapping("/login")
    public String login(ModelMap mmap) {
        return "home/login";
    }

    // 登录首页
    @GetMapping("/index")
    public String index(ModelMap mmap, Principal principal) {
        String name = principal.getName();
        mmap.put("user", name);
        mmap.put("copyrightYear", DateUtils.getYearorMonthorDay(DateUtils.FORMAT_YYYY));
        return "home/index";
    }

    // 首页显示
    @GetMapping("/home")
    public String home(ModelMap mmap) {
        return "home/main";
    }
}
