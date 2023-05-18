package com.kcmp.ck.flow.dao.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by kikock
 * 页面URL工具类
 * @author kikock
 * @email kikock@qq.com
 **/
public class PageUrlUtil {
    /**
     * 拼接完整的URL
     * @param baseAddress 基地址
     * @param pageUrl 页面地址
     * @return 完整URL
     */
    public static String buildUrl(String baseAddress, String pageUrl){
        if (StringUtils.endsWith(baseAddress, "/")){
            baseAddress = StringUtils.substringBeforeLast(baseAddress, "/");
        }
        if (StringUtils.startsWith(pageUrl, "/")){
            pageUrl = StringUtils.substringAfter(pageUrl, "/");
        }
        return baseAddress + "/" + pageUrl;
    }

    /**
     * 拼接完整的URL
     * @param baseAddress 基地址
     * @param pageUrl 页面地址
     * @param params 参数
     * @return 完整URL
     */
    public static String buildUrl(String baseAddress, String pageUrl, String params){
        String address = buildUrl(baseAddress, pageUrl);
        if (StringUtils.containsAny(address, "?")){
            return address + "&" + params;
        }
        return address + "?" + params;
    }
}
