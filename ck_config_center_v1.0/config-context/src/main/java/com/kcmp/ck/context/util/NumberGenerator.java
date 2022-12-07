package com.kcmp.ck.context.util;


import com.kcmp.ck.config.util.ApiClient;
import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.context.common.ConfigConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 序列编号工具类.
 *
 * @author kikock
 * @version 1.0.0
 */
public class NumberGenerator {

    private NumberGenerator() {
    }

    /**
     * 获取一个序列编号.
     *
     * @param entityClass 业务实体类
     * @return 序列编号
     */
    public static String getNumber(Class entityClass) {
        if (Objects.isNull(entityClass)) {
            throw new IllegalArgumentException("业务实体类名不能为空");
        }
        return getNumber(entityClass.getName(), null);
    }

    /**
     * 获取一个序列编号并初始化.
     *
     * @param entityClass 业务实体类
     * @return 序列编号
     */
    public static String getNumberAndInit(Class entityClass) {
        if (Objects.isNull(entityClass)) {
            throw new IllegalArgumentException("业务实体类名不能为空");
        }
        return getNumberAndInit(entityClass.getName(), null);
    }

    /**
     * 获取一个序列编号(有隔离码).
     *
     * @param entityClass   业务实体类
     * @param isolationCode 隔离码
     * @return 序列编号
     */
    public static String getNumber(Class entityClass, String isolationCode) {
        if (Objects.isNull(entityClass)) {
            throw new IllegalArgumentException("业务实体类名不能为空");
        }
        return getNumber(entityClass.getName(), isolationCode);
    }

    /**
     * 获取一个序列编号并初始化(有隔离码).
     *
     * @param entityClass   业务实体类
     * @param isolationCode 隔离码
     * @return 序列编号
     */
    public static String getNumberAndInit(Class entityClass, String isolationCode) {
        if (Objects.isNull(entityClass)) {
            throw new IllegalArgumentException("业务实体类名不能为空");
        }
        return getNumberAndInit(entityClass.getName(), isolationCode);
    }

    /**
     * 获取一个序列编号.
     *
     * @param entityClassName 业务实体类名(含包路径)
     * @return 序列编号
     */
    public static String getNumber(String entityClassName) {
        return getNumber(entityClassName, null);
    }

    /**
     * 获取一个序列编号并初始化.
     *
     * @param entityClassName 业务实体类名(含包路径)
     * @return 序列编号
     */
    public static String getNumberAndInit(String entityClassName) {
        return getNumberAndInit(entityClassName, null);
    }

    /**
     * 获取一个序列编号.
     * 有隔离码可为空
     *
     * @param entityClassName 业务实体类名(含包路径)
     * @param isolationCode   隔离码
     * @return 序列编号
     */
    public static String getNumber(String entityClassName, String isolationCode) {
        if (StringUtils.isBlank(entityClassName)) {
            throw new IllegalArgumentException("业务实体类名不能为空");
        }
        String path;
        Map<String, Object> params = new HashMap<>();
        params.put("envCode", ContextUtil.getRunningEnv());
        params.put("entityClassName", entityClassName);
        if (StringUtils.isNotBlank(isolationCode)) {
            params.put("isolationCode", isolationCode);
            path = "/serialNumberConfig/getNumberWithIsolation";
        } else {
            path = "/serialNumberConfig/getNumber";
        }
        return ApiClient.getEntityViaProxy(ConfigConstants.CONFIG_CENTER_API, path, String.class, params);
    }

    /**
     * 获取一个序列编号并初始化.
     * 有隔离码可为空
     *
     * @param entityClassName 业务实体类名(含包路径)
     * @param isolationCode   隔离码
     * @return 序列编号
     */
    public static String getNumberAndInit(String entityClassName, String isolationCode) {
        if (StringUtils.isBlank(entityClassName)) {
            throw new IllegalArgumentException("业务实体类名不能为空");
        }
        String path;
        Map<String, Object> params = new HashMap<>();
        params.put("envCode", ContextUtil.getRunningEnv());
        params.put("entityClassName", entityClassName);
        if (StringUtils.isNotBlank(isolationCode)) {
            params.put("isolationCode", isolationCode);
            path = "/serialNumberConfig/getNumberWithIsolationAndInit";
        } else {
            path = "/serialNumberConfig/getNumberAndInit";
        }
        return ApiClient.getEntityViaProxy(ConfigConstants.CONFIG_CENTER_API, path, String.class, params);
    }

}
