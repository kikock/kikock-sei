package com.kcmp.ck.flow.common.web.controller;

import com.kcmp.ck.log.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.PatternEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by kikock
 * 基础控制器
 * @author kikock
 * @email kikock@qq.com
 **/
public abstract class BaseController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
    /**
     * 重定向前缀
     */
    public static final String REDIRECT = "redirect:";
    /**
     * 转发前缀
     */
    public static final String FORWARD = "forward:";

    @InitBinder
    public void initListBinder(WebDataBinder binder) {
        //因为springmvc默认只支持256个对象映射，加入以下代码即可解决
        binder.setAutoGrowCollectionLimit(1024);
        //将前台传递过来的日期格式的字符串，自动转化为Date类型
        binder.registerCustomEditor(Date.class, new PatternEditor());

        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String value) {
                if (StringUtils.isNotBlank(value)) {
                    setValue(value);
                } else {
                    setValue(null);
                }
            }
        });
        binder.registerCustomEditor(Double.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String value) {
                try {
                    if (StringUtils.isNotBlank(value)) {
                        setValue((new BigDecimal(value)).doubleValue());
                    } else {
                        setValue(null);
                    }
                } catch (Exception e) {
                    setValue(null);
                }
            }
        });
        binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String value) {
                try {
                    if (StringUtils.isNotBlank(value)) {
                        setValue(new BigDecimal(value));
                    } else {
                        setValue(null);
                    }
                } catch (Exception e) {
                    setValue(null);
                }
            }
        });
    }

    /**
     * 获取处理中文乱码后的文件名
     * @param request 请求
     * @param filename 文件名
     * @return 文件名
     * @throws Exception
     */
    private String getFilename(HttpServletRequest request, String filename) throws Exception {
        // 处理中文文件名乱码问题
        try {
            /*
             * IE，通过URLEncoder对filename进行UTF8编码
             * 其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1
             */
            if (StringUtils.containsAny(request.getHeader("User-Agent").toLowerCase(), "msie", "edge")) {
                filename = URLEncoder.encode(filename, "UTF-8");
            } else {
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.error("文件名编码错误", e);
        }
        //返回一个进行url编码的文件名
        return filename;
    }
}
