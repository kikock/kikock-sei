package com.kcmp.ck.util;

import com.kcmp.ck.annotation.AppModule;
import com.kcmp.ck.context.ContextUtil;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by kikock
 * 平台调用API服务的客户端工具
 * @email kikock@qq.com
 **/
@Deprecated
public class WebApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebApiClient.class);

    /**
     * 获取API服务的应用模块
     * @param apiClass API服务接口类
     * @param <T>      API服务接口泛型
     * @return 应用模块
     */
    private static <T> String getAppBaseUrl(Class<T> apiClass) {
        String packageName = apiClass.getPackage().getName();
        AppModule appModuleAnnotation = null;
        Annotation[] annotations = apiClass.getPackage().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof AppModule) {
                appModuleAnnotation = (AppModule) annotation;
                break;
            }
        }

        //获取应用模块注解
        if (appModuleAnnotation == null) {
            throw new ExceptionInInitializerError(String.format("API服务包[%s]没有配置应用模块注解！", packageName));
        }
        String appModuleCode = appModuleAnnotation.value();
        String appBaseUrl = ContextUtil.getAppBaseUrl(appModuleCode);
        if (Objects.isNull(appBaseUrl)) {
            throw new ExceptionInInitializerError(String.format("系统没有配置应用模块：[%s]！", appModuleCode));
        }
        String contextPath = ContextUtil.getContextPath(appBaseUrl);
        return ContextUtil.getApiGateway() + contextPath;
    }

    /**
     * 配置客户端Providers
     * @return Providers
     */
    private static List<Object> configProviders() {
        List<Object> providers = new ArrayList<>();
        //平台API服务使用的JSON序列化提供类
        providers.add(new ApiRestJsonProvider());
        //API会话检查的客户端过滤器
        providers.add(new TokenClientRequestFilter());
        //Zipkin ClientFilter
        if (ContextUtil.containsBean(ZipkinTracingClientFilter.BEAN_ID)) {
            providers.add(ContextUtil.getBean(ZipkinTracingClientFilter.BEAN_ID));
        }
        return providers;
    }

    /**
     * 创建API服务代理
     * @param apiClass API服务接口类
     * @param <T>      API服务接口泛型
     * @return 服务代理
     */
    public static <T> T createProxy(Class<T> apiClass) {
        //获取服务基地址
        String baseAddress = getAppBaseUrl(apiClass);
        //记录API调用日志
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("调用ApiClient 基地址:[{}],ApiClass:[{}]。", baseAddress, apiClass.getName());
        }
        return JAXRSClientFactory.create(baseAddress, apiClass, configProviders());
    }

    /**
     * 创建应用模块的API服务代理
     * @param path          API路径（含方法路径）
     * @param appModuleCode 应用模块代码
     * @return 服务代理
     */
    public static WebClient createProxy(String appModuleCode, String path) {
        //获取服务基地址
        String appBaseUrl = ContextUtil.getAppBaseUrl(appModuleCode);
        String contextPath = ContextUtil.getContextPath(appBaseUrl);
        String baseAddress = ContextUtil.getApiGateway() + contextPath;
        //记录API调用日志
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("调用ApiClient 基地址:[{}],ApiPath:[{}]。", baseAddress, path);
        }
        WebClient client = WebClient.create(baseAddress, configProviders());
        client.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        client.path(path);
        return client;
    }

    /**
     * 创建应用模块的API服务客户端代理
     * @param appModuleCode 应用模块代码
     * @param path          API路径（含方法路径）
     * @param params        输入参数(K-参数名，V-参数值)
     * @return 返回结果
     */
    private static WebClient createClient(String appModuleCode, String path, Map<String, Object> params) {
        WebClient client = createProxy(appModuleCode, path);
        //拼装请求路径
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> p : params.entrySet()) {
                client.query(p.getKey(), p.getValue());
            }
        }
        return client;
    }

    /**
     * 创建API服务代理并调用GET方法获取实体
     * @param appModuleCode 应用模块代码
     * @param path          API路径（含方法路径）
     * @param entityClass   获取实体的类型
     * @param params        输入参数(K-参数名，V-参数值)
     * @param <T>           获取实体的泛型
     * @return 获取的实体
     */
    public static <T> T getEntityViaProxy(String appModuleCode, String path, Class<T> entityClass, Map<String, Object> params) {
        WebClient client = createClient(appModuleCode, path, params);
        return client.get(entityClass);
    }

    /**
     * 创建API服务代理并调用GET方法获取实体
     * @param appModuleCode 应用模块代码
     * @param path          API路径（含方法路径）
     * @param entityClass   获取实体的类型(泛型)
     * @param params        输入参数(K-参数名，V-参数值)
     * @param <T>           获取实体的泛型
     * @return 获取的实体
     */
    public static <T> T getEntityViaProxy(String appModuleCode, String path, GenericType<T> entityClass, Map<String, Object> params) {
        WebClient client = createClient(appModuleCode, path, params);
        return client.get(entityClass);
    }

    /**
     * 创建API服务代理调用POST方法并返回执行结果
     * @param appModuleCode 应用模块代码
     * @param path          API路径（含方法路径）
     * @param resultClass   返回结果的类型
     * @param input         输入参数
     * @param <T>           返回结果的泛型
     * @return 返回结果
     */
    public static <T> T postViaProxyReturnResult(String appModuleCode, String path, Class<T> resultClass, Object input) {
        WebClient client = createProxy(appModuleCode, path);
        return client.post(input, resultClass);
    }

    /**
     * 创建API服务代理调用POST方法并返回执行结果
     * @param appModuleCode 应用模块代码
     * @param path          API路径（含方法路径）
     * @param resultClass   返回结果的类型(泛型)
     * @param input         输入参数
     * @param <T>           返回结果的泛型
     * @return 返回结果
     */
    public static <T> T postViaProxyReturnResult(String appModuleCode, String path, GenericType<T> resultClass, Object input) {
        WebClient client = createProxy(appModuleCode, path);
        return client.post(input, resultClass);
    }

    /**
     * 创建API服务代理调用POST方法并返回执行结果
     * @param appModuleCode 应用模块代码
     * @param path          API路径（含方法路径）
     * @param resultClass   返回结果的类型
     * @param params        输入参数(K-参数名，V-参数值)
     * @param <T>           返回结果的泛型
     * @return 返回结果
     */
    public static <T> T postViaProxyReturnResult(String appModuleCode, String path, Class<T> resultClass, Map<String, Object> params) {
        WebClient client = createClient(appModuleCode, path, params);
        return client.post(null, resultClass);
    }

    /**
     * 创建API服务代理调用POST方法并返回执行结果
     * @param appModuleCode 应用模块代码
     * @param path          API路径（含方法路径）
     * @param resultClass   返回结果的类型(泛型)
     * @param params        输入参数(K-参数名，V-参数值)
     * @param <T>           返回结果的泛型
     * @return 返回结果
     */
    public static <T> T postViaProxyReturnResult(String appModuleCode, String path, GenericType<T> resultClass, Map<String, Object> params) {
        WebClient client = createClient(appModuleCode, path, params);
        return client.post(null, resultClass);
    }

    /**
     * 创建API服务代理并调用GET方法获取实体
     * @param url         API路径（含方法路径）
     * @param entityClass 获取实体的类型
     * @param params      输入参数(K-参数名，V-参数值)
     * @param <T>         获取实体的泛型
     * @return 获取的实体
     */
    public static <T> T getEntityViaProxy(String url, Class<T> entityClass, Map<String, Object> params) {
        WebClient client = createClient(url, params);
        return client.get(entityClass);
    }

    /**
     * 创建API服务代理并调用GET方法获取实体
     * @param url         API路径（含方法路径）
     * @param entityClass 获取实体的类型(泛型)
     * @param params      输入参数(K-参数名，V-参数值)
     * @param <T>         获取实体的泛型
     * @return 获取的实体
     */
    public static <T> T getEntityViaProxy(String url, GenericType<T> entityClass, Map<String, Object> params) {
        WebClient client = createClient(url, params);
        return client.get(entityClass);
    }

    /**
     * 创建API服务代理调用POST方法并返回执行结果
     * @param url         API路径（含方法路径）
     * @param resultClass 返回结果的类型
     * @param input       输入参数
     * @param <T>         返回结果的泛型
     * @return 返回结果
     */
    public static <T> T postViaProxyReturnResult(String url, Class<T> resultClass, Object input) {
        WebClient client = createProxy(url);
        return client.post(input, resultClass);
    }

    /**
     * 创建API服务代理调用POST方法并返回执行结果
     * @param url         API路径（含方法路径）
     * @param resultClass 返回结果的类型(泛型)
     * @param input       输入参数
     * @param <T>         返回结果的泛型
     * @return 返回结果
     */
    public static <T> T postViaProxyReturnResult(String url, GenericType<T> resultClass, Object input) {
        WebClient client = createProxy(url);
        return client.post(input, resultClass);
    }

    /**
     * 创建API服务代理调用POST方法并返回执行结果
     * @param url         API路径（含方法路径）
     * @param resultClass 返回结果的类型
     * @param params      输入参数(K-参数名，V-参数值)
     * @param <T>         返回结果的泛型
     * @return 返回结果
     */
    public static <T> T postViaProxyReturnResult(String url, Class<T> resultClass, Map<String, Object> params) {
        WebClient client = createClient(url, params);
        return client.post(null, resultClass);
    }

    /**
     * 创建API服务代理调用POST方法并返回执行结果
     * @param url         API路径（含方法路径）
     * @param resultClass 返回结果的类型(泛型)
     * @param params      输入参数(K-参数名，V-参数值)
     * @param <T>         返回结果的泛型
     * @return 返回结果
     */
    public static <T> T postViaProxyReturnResult(String url, GenericType<T> resultClass, Map<String, Object> params) {
        WebClient client = createClient(url, params);
        return client.post(null, resultClass);
    }

    /**
     * 创建应用模块的API服务代理
     * @param url API路径（含方法路径）
     * @return 服务代理
     */
    public static WebClient createProxy(String url) {
        //记录API调用日志
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("调用ApiClient 地址:[{}]。", url);
        }
        WebClient client = WebClient.create(url, configProviders());
        client.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        return client;
    }

    /**
     * 创建应用模块的API服务客户端代理
     * @param url    API路径（含方法路径）
     * @param params 输入参数(K-参数名，V-参数值)
     * @return 返回结果
     */
    private static WebClient createClient(String url, Map<String, Object> params) {
        WebClient client = createProxy(url);
        //拼装请求路径
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> p : params.entrySet()) {
                client.query(p.getKey(), p.getValue());
            }
        }
        return client;
    }
}
