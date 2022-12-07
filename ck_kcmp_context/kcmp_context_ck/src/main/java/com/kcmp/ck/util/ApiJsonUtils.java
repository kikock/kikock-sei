package com.kcmp.ck.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kikock
 * ApiJSON的工具类
 * @email kikock@qq.com
 **/
@SuppressWarnings("unchecked")
public abstract class ApiJsonUtils {

    /**
     * 将json通过类型转换成对象
     * @param <T>   泛型
     * @param json  json字符串
     * @param clazz 泛型类型
     * @return 返回对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (null == json || StringUtils.EMPTY.equals(json)) {
            return null;
        } else {
            try {
                ObjectMapper om = mapper();
                return clazz.equals(String.class) ? (T) json : om.readValue(json, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 将Json反序列化为List<T>
     * @param <T>   泛型
     * @param json  json字符串
     * @param clazz 集合元素类型
     * @return 返回集合对象
     */
    public static <T> List<T> fromJson2List(String json, Class<T> clazz) {
        if (null == json || StringUtils.EMPTY.equals(json)) {
            return null;
        } else {
            try {
                ObjectMapper om = mapper();
                JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, clazz);
                return om.readValue(json, javaType);
                //return om.readValue(json, new TypeReference<List<T>>() {});
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 将对象转换成json
     * @param <T> 泛型
     * @param src 对象
     * @return 返回json字符串
     */
    public static <T> String toJson(T src) {
        return toJson(src, null, (String[]) null);
    }

    /**
     * 将对象转换成json
     * @param <T>        泛型
     * @param src        对象
     * @param properties 过滤属性(排除的属性)
     * @return 返回json字符串
     */
    public static <T> String toJson(T src, String... properties) {
        return toJson(src, null, properties);
    }

    /**
     * 将对象转换成json, 可以设置输出属性
     * <ul>
     * <li>{@link JsonInclude.Include ALWAYS 全部列入}</li>
     * <li>{@link JsonInclude.Include NON_DEFAULT 字段和对象默认值相同的时候不会列入}</li>
     * <li>{@link JsonInclude.Include NON_EMPTY 字段为NULL或者""的时候不会列入}</li>
     * <li>{@link JsonInclude.Include NON_NULL 字段为NULL时候不会列入}</li>
     * </ul>
     * @param <T>       泛型
     * @param src       对象
     * @param inclusion 传入一个枚举值, 设置输出属性
     * @return 返回json字符串
     */
    public static <T> String toJson(T src, JsonInclude.Include inclusion, String... properties) {
        if (null == src) {
            return null;
        }

        if (src instanceof String) {
            return (String) src;
        } else {
            try {
                ObjectMapper om = generateMapper((null == inclusion) ? JsonInclude.Include.ALWAYS : inclusion);
                if (null != properties && properties.length > 0) {
                    FilterProvider fp = new SimpleFilterProvider().addFilter("customFilter", SimpleBeanPropertyFilter.serializeAllExcept(properties));
                    om.setFilterProvider(fp);
                }

                return om.writeValueAsString(src);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 将对象转换成json, 传入配置对象
     * @param <T>    泛型
     * @param src    对象
     * @param mapper 配置对象
     * @return 返回json字符串
     * @throws IOException IOException
     * @see ObjectMapper
     */
    public static <T> String toJson(T src, ObjectMapper mapper) throws IOException {
        if (null != mapper) {
            if (src instanceof String) {
                return (String) src;
            } else {
                return mapper.writeValueAsString(src);
            }
        } else {
            return null;
        }
    }

    /**
     * 通过路径获取节点值
     * @param json 需处理的Json字符串
     * @param path 获取路径
     * @return 返回指定路径获取节点值
     */
    public String getNodeString(String json, String path) {
        String nodeStr;
        if (json == null || StringUtils.EMPTY.equals(json.trim())) {
            return null;
        }
        if (path == null || StringUtils.EMPTY.equals(path.trim())) {
            return json;
        }

        try {
            ObjectMapper om = new ObjectMapper();
            JsonNode jn = om.readTree(json);
            nodeStr = jn.path(path).toString();
        } catch (Exception e) {
            throw new RuntimeException("解析json错误");
        }

        return nodeStr;
    }

    /**
     * 返回ObjectMapper对象, 用于定制性的操作
     * @return
     */
    public static ObjectMapper mapper() {
        return generateMapper(JsonInclude.Include.ALWAYS);
    }

    /**
     * 通过Inclusion创建ObjectMapper对象
     * @param include 传入一个枚举值, 设置输出属性
     * @return 返回ObjectMapper对象
     */
    private static ObjectMapper generateMapper(JsonInclude.Include include) {
        ObjectMapper objectMapper = JsonUtils.mapper();

        // 设置输出时包含属性的风格
        objectMapper.setSerializationInclusion(include);

        boolean isHibernate = true;
        try {
            //检查是否是Hibernate环境
            Class.forName("org.hibernate.proxy.HibernateProxy");
        } catch (ClassNotFoundException ignored) {
            isHibernate = false;
        }
        if (isHibernate) {
            //解决 hibernate 懒加载序列化问题
            Hibernate5Module hibernate5Module = new Hibernate5Module();
            hibernate5Module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
            hibernate5Module.disable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
            objectMapper.registerModule(hibernate5Module);
        }
        return objectMapper;
    }

}
