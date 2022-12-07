package com.kcmp.ck.context;

import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * Created by kikock
 * 上下文抽象类
 * @email kikock@qq.com
 **/
public class BaseContextSupport extends BaseApplicationContext {

    /**
     * 获取全局参数.
     */
    public static Properties getGlobalProperties(String key) {
        LinkedHashMap dataMap = getProperty(key, LinkedHashMap.class);
        Properties properties = new Properties();
        if (dataMap != null) {
            properties.putAll(dataMap);
        }
        return properties;
    }

    /**
     * 获取全局参数.
     * @param key 参数key
     * @return 返回参数值
     */
    public static String getGlobalProperty(String key) {
        return getGlobalProperty(key, key);
    }

    /**
     * 获取全局参数.
     * @param key      全局参数配置key
     * @param paramKey 参数key
     * @return 返回参数值
     */
    public static String getGlobalProperty(String key, String paramKey) {
        return getGlobalProperty(key, paramKey, null);
    }

    /**
     * 获取全局参数.
     * @param key      全局参数配置key
     * @param paramKey 参数key
     * @return 返回参数值
     */
    public static String getGlobalProperty(String key, String paramKey, String defaultValue) {
        String result = null;
        if (StringUtils.isNotBlank(key)) {
            Object obj = getProperty(key, Object.class);
            if (obj != null) {
                if (obj instanceof LinkedHashMap) {
                    LinkedHashMap dataMap = (LinkedHashMap) obj;
                    result = (String) dataMap.get(paramKey);
                    if (StringUtils.isBlank(result)) {
                        result = defaultValue;
                    }
                } else {
                    result = String.valueOf(obj);
                }
            } else {
                result = defaultValue;
            }
        }
        return result;
    }

    /**
     * 给定属性键是否可用于解析
     * @param key 属性key
     * @return 存在返回true，反之false
     */
    public static boolean containsProperty(String key) {
        boolean existed = false;
        if (StringUtils.isNotBlank(key)) {
            existed = BaseApplicationContext.containsProperty(key);
        }
        return existed;
    }

    /**
     * 获取当前运行环境.
     * @return 返回当前运行环境。
     */
    public static String getRunningEnv() {
        String env = getGlobalProperty("SELF_APP_SERVICE", "SELF_ENV");
        return env;
    }

    /**
     * 当前环境是否是开发环境.
     * @return 返回true，则是开发环境，反之不是。
     */
    public static boolean isDevEnv() {
        return StringUtils.containsIgnoreCase(getRunningEnv(), "DEV");
    }

    /**
     * 获取当前应用配置是否从本地加载.
     * @return 当前应用配置是否从本地加载。
     */
    public static boolean isLocalConfig() {
        return getProperty("isLocalConfig", Boolean.class);
    }

    /**
     * 获取当前应用标识.
     * @return 返回当前应用标识。
     */
    public static String getAppId() {
        return getGlobalProperty(ConfigConstants.ENV_ECMP_APP_ID);
    }

    /**
     * 获取当前应用代码.
     * @return 返回当前应用代码。
     */
    public static String getAppCode() {
        String appCode = getGlobalProperty("SELF_APP_SERVICE", "SELF_APP");
        return appCode;
    }

    /**
     * API服务网关地址
     * @return API服务网关地址
     */
    public static String getApiGateway() {
        String apiGatewayHost = getGlobalProperty("API_GATEWAY_HOST");
        if (apiGatewayHost.endsWith("/")) {
            apiGatewayHost = apiGatewayHost.substring(0, apiGatewayHost.length() - 1);
        }
        return apiGatewayHost;
    }

    /**
     * 监控中心地址
     * @return 监控中心地址
     */
    public static String getAdminUrl() {
        String adminHost = getGlobalProperty("ADMIN_HOST");
        if (StringUtils.isNotBlank(adminHost) && adminHost.endsWith("/")) {
            adminHost = adminHost.substring(0, adminHost.length() - 1);
        }
        return adminHost;
    }

    /**
     * API服务网关地址
     * @return API服务网关地址
     */
    public static String getContextPath(String urlPath) {
        if (StringUtils.isNotBlank(urlPath)) {
            try {
                URL url = new URL(urlPath);
                return url.getPath();
            } catch (MalformedURLException ignored) {
            }
        }
        return urlPath;
    }

    /**
     * 获取当前应用基地址.
     * @return 返回当前应用基地址。
     */
    public static String getAppBaseUrl() {
        return getGlobalProperty(getAppCode());
    }

    /**
     * 获取指定应用基地址.
     * @param appCode 应用代码
     * @return 返回指定应用的基地址。
     */
    public static String getAppBaseUrl(String appCode) {
        return getGlobalProperty(appCode);
    }
}
