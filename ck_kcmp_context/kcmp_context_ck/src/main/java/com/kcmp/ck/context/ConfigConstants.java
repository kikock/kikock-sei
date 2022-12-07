package com.kcmp.ck.context;

/**
 * Created by kikock
 * 平台配置常量
 * @email kikock@qq.com
 **/
public abstract class ConfigConstants {

    ///////////////////////// applicationConfig.properties配置 start ////////////////////////////////
    /**
     * 应用名称
     */
    public static final String SYS_NAME = "SYS_NAME";
    /**
     * 应用服务AppId
     */
    public static final String ENV_KCMP_APP_ID = "KCMP_APP_ID";
    /**
     * 云平台配置中心zookeeper服务地址
     */
    public static final String ENV_KCMP_CONFIG_CENTER = "KCMP_CONFIG_CENTER";
    /**
     * 配置中心zookeeper的节点命名空间
     */
    public static final String ZK_NAME_SPACE = "com.center.config";
    ///////////////////////// applicationConfig.properties配置 end ////////////////////////////////

    ///////////////////////// 全局配置参数 start ////////////////////////////////
    /**
     * 全局配置参数key，BASIC_API服务基路径
     */
    public static final String BASIC_API = "BASIC_API";
    /**
     * 全局配置参数key，CONFIG_CENTER_API服务基路径
     */
    public static final String CONFIG_CENTER_API = "CONFIG_CENTER_API";
    /**
     * 全局配置参数key，认证中心相关配置
     */
    public static final String PARAM_AUTH_CENTER = "AUTH_CENTER";
    /**
     * 全局配置参数key，API_PACKAGE相关配置
     */
    public static final String PARAM_API_PACKAGE = "API_PACKAGE";
    /**
     * 全局配置参数key，data source相关配置
     */
    public static final String PARAM_DATASOURCE = "DATASOURCE";
    public static final String PARAM_SECOND_LEVEL_CACHE_HOST = "second_level_cache_host";
    public static final String PARAM_SECOND_LEVEL_CACHE_PORT = "second_level_cache_port";
    public static final String PARAM_SECOND_LEVEL_CACHE_PW = "second_level_cache_password";
    public static final String PARAM_SECOND_LEVEL_CACHE_DB = "second_level_cache_db";
    /**
     * 全局配置参数key，业务缓存相关配置
     */
    public static final String PARAM_BIZ_CACHE = "BIZ_CACHE";
    /**
     * 全局配置参数key，会话缓存相关配置
     */
    public static final String PARAM_SESSION_CACHE = "KCMP_SESSION_CACHE";
    /**
     * 全局配置参数key，EDM_MONGODB相关配置
     */
    public static final String PARAM_EDM_MONGODB = "EDM_MONGODB";
    public static final String SWITCH_EDM_KEY = "com.kcmp.edm.jx.enable";
    /**
     * 全局配置参数key，ELASTIC_SEARCH相关配置
     */
    public static final String PARAM_ELASTIC_SEARCH = "ELASTIC_SEARCH";
    /**
     * 全局配置参数key，web相关配置
     */
    public static final String PARAM_WEB_CONFIG = "WEB_CONFIG";
    /**
     * 局配置参数key，kafka参数配置键
     */
    public static final String MQ_CONFIG_KEY = "MQ_PRODUCER";
    /**
     * 全局配置参数key，是否启用验证码，boolean类型，默认:true
     */
    public static final String PARAM_CAPTCHA_ENABLED = "captcha.enabled";
    /**
     * 全局配置参数key，验证码参数key
     * String，默认:captchaEnabledKey
     */
    public static final String PARAM_CAPTCHA_ENABLED_KEY = "captcha.enabled.key";
    /**
     * 登录账号密码加密传输
     */
    public static final String PARAM_LOGIN_ENCRYPT = "login.encrypt";
    /**
     * 静态资源服务器的全局配置参数key，平台统一配置
     */
    public static final String STATIC_RESOURCE_CDN = "static.resource.url";
    /**
     * 全局配置参数key，分发配置
     */
    public static final String ZIPKIN_SERVER_KEY = "ZIPKIN_SERVER";
    /**
     * 全局会话超时时间（秒）
     */
    public static final String JX_GLOBAL_SESSION_TIMEOUT = "server.servlet.session.timeout";
    ///////////////////////// 全局配置参数 end ////////////////////////////////
}
