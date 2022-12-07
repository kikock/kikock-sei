package com.kcmp.ck.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by kikock
 * 属性文件操作工具类
 * @email kikock@qq.com
 **/
public class PropertyUtils {
    private static PropertyUtils instance = new PropertyUtils();
    private boolean isSearchSystemProperty = false;
    private Properties properties;

    private PropertyUtils() {
        properties = new Properties();
    }

    public static PropertyUtils getInstance() {
        return instance;
    }

    public void loadAllPropertiesFromClassLoader(String... resourceNames) {
        if (resourceNames == null || resourceNames.length == 0) {
            resourceNames = new String[]{"config.xml", "config.properties"};
        }
        try {
            for (String resourceName : resourceNames) {
                Enumeration urls = PropertyUtils.class.getClassLoader().getResources(resourceName);
                while (urls.hasMoreElements()) {
                    URL url = (URL) urls.nextElement();
                    InputStream input = null;
                    try {
                        URLConnection con = url.openConnection();
                        con.setUseCaches(false);
                        input = con.getInputStream();
                        if (resourceName.endsWith(".xml")) {
                            properties.loadFromXML(input);
                        } else {
                            properties.load(input);
                        }
                    } finally {
                        if (input != null) {
                            input.close();
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            if (properties == null)
                throw new RuntimeException("Properties file loading failed: " + Arrays.toString(resourceNames));
        }
    }

    private String _getProperty(String key, String defaultValue) {
        String value = null;
        if (isSearchSystemProperty) {
            value = System.getProperty(key);
        }
        if (value == null || "".equals(value.trim())) {
            value = properties.getProperty(key);
        }
        return value == null || "".equals(value.trim()) ? defaultValue : value;
    }

    private String _getProperty(String key) {
        checkPropertyLoading();
        return getProperty(key, null);
    }

    private String _getRequiredProperty(String key) {
        String value = getProperty(key);
        if (value == null || "".equals(value.trim())) {
            throw new IllegalStateException("required property is blank by key=" + key);
        }
        return value;
    }

    private Integer _getInt(String key) {
        if (getProperty(key) == null) {
            return null;
        }
        return Integer.parseInt(getRequiredProperty(key));
    }

    private int _getInt(String key, int defaultValue) {
        if (getProperty(key) == null) {
            return defaultValue;
        }
        return Integer.parseInt(getRequiredProperty(key));
    }

    private int _getRequiredInt(String key) {
        return Integer.parseInt(getRequiredProperty(key));
    }

    private Boolean _getBoolean(String key) {
        if (getProperty(key) == null) {
            return null;
        }
        return Boolean.parseBoolean(getRequiredProperty(key));
    }

    private boolean _getBoolean(String key, boolean defaultValue) {
        if (getProperty(key) == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(getRequiredProperty(key));
    }

    private boolean _getRequiredBoolean(String key) {
        return Boolean.parseBoolean(getRequiredProperty(key));
    }

    private Properties _setProperty(String key, String value) {
        checkPropertyLoading();
        properties.setProperty(key, value);
        return properties;
    }

    private void _clear() {
        checkPropertyLoading();
        properties.clear();
    }

    private Set<Map.Entry<Object, Object>> _entrySet() {
        checkPropertyLoading();
        return properties.entrySet();
    }

    private Enumeration<?> _propertyNames() {
        checkPropertyLoading();
        return properties.propertyNames();
    }

    private void checkPropertyLoading() {
        if (properties == null)
            throw new RuntimeException("Before use, you must load the properties file by calling loadAllPropertiesFromClassLoader (string) method.");
    }

    /**
     * properties不为null表示已装载
     * @return 返回true表示已装载，反之未装载。
     */
    public boolean isLoadProperty() {
        return properties != null && properties.size() > 0;
    }

    public boolean isSearchSystemProperty() {
        return isSearchSystemProperty;
    }

    public void setSearchSystemProperty(boolean searchSystemProperty) {
        isSearchSystemProperty = searchSystemProperty;
    }

    //**********************************************************************************

    public static String getProperty(String key, String defaultValue) {
        return PropertyUtils.getInstance()._getProperty(key, defaultValue);
    }

    public static String getProperty(String key) {
        return PropertyUtils.getInstance()._getProperty(key);
    }

    public static String getRequiredProperty(String key) {
        return PropertyUtils.getInstance()._getRequiredProperty(key);
    }

    public static Integer getInt(String key) {
        return PropertyUtils.getInstance()._getInt(key);
    }

    public static int getInt(String key, int defaultValue) {
        return PropertyUtils.getInstance()._getInt(key, defaultValue);
    }

    public static int getRequiredInt(String key) {
        return PropertyUtils.getInstance()._getRequiredInt(key);
    }

    public static Boolean getBoolean(String key) {
        return PropertyUtils.getInstance()._getBoolean(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return PropertyUtils.getInstance()._getBoolean(key, defaultValue);
    }

    public static boolean getRequiredBoolean(String key) {
        return PropertyUtils.getInstance()._getRequiredBoolean(key);
    }

    public static Properties setProperty(String key, String value) {
        return PropertyUtils.getInstance()._setProperty(key, value);
    }

    public static void clear() {
        PropertyUtils.getInstance()._clear();
    }

    public static Set<Map.Entry<Object, Object>> entrySet() {
        return PropertyUtils.getInstance()._entrySet();
    }

    public static Enumeration<?> propertyNames() {
        return PropertyUtils.getInstance()._propertyNames();
    }
}
