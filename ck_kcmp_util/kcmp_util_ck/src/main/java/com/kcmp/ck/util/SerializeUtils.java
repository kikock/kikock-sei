package com.kcmp.ck.util;

import org.apache.commons.codec.binary.Base64;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by kikock
 * 序列化工具类
 * @email kikock@qq.com
 **/
@SuppressWarnings("unchecked")
public class SerializeUtils {

    /**
     * 对象序列化成字符串
     * @param object 对象
     * @return 返回字符串
     */
    public static String serialize4Str(Object object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            return Base64.encodeBase64String(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("serialize object error", e);
        }
    }

    /**
     * 字符串反序列化为对象
     * @param <T>  泛型
     * @param sStr 字符串
     * @return 返回对象
     */
    public static <T> T unserialize4Str(String sStr) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(sStr));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (T) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("unserialize object error", e);
        }
    }

    /**
     * 对象序列化为二进制
     * @param object 对象
     * @return 返回二进制
     */
    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException("serialize object error", e);
        }
    }

    /**
     * @param bytes 二进制数据
     * @return 二进制反序列化为对象
     */
    public static Object unserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream bais;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("unserialize object error", e);
        }
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws java.beans.IntrospectionException           如果分析类属性失败
     * @throws IllegalAccessException                      如果实例化 JavaBean 失败
     * @throws java.lang.reflect.InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> convertBean(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    if (result instanceof Collection) {
                        List list = new ArrayList();
                        Collection collection = (Collection) result;
                        for (Object obj : collection) {
                            list.add(convertBean(obj));
                        }
                        returnMap.put(propertyName, list);
                    } else {
                        returnMap.put(propertyName, result);
                    }
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map  包含属性值的 map
     * @param <T>  泛型
     * @return 转化出来的 JavaBean 对象
     * @throws java.beans.IntrospectionException           如果分析类属性失败
     * @throws IllegalAccessException                      如果实例化 JavaBean 失败
     * @throws InstantiationException                      如果实例化 JavaBean 失败
     * @throws java.lang.reflect.InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static <T> T convertMap(Class<T> type, Map<String, Object> map) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        T object = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (map.containsKey(propertyName)) {
                Object[] args = new Object[1];
                args[0] = map.get(propertyName);
                // try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                try {
                    descriptor.getWriteMethod().invoke(object, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
}
