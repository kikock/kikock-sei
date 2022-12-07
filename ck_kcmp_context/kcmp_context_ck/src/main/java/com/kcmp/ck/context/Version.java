package com.kcmp.ck.context;

import java.util.Objects;

/**
 * Created by kikock
 * 版本
 * @email kikock@qq.com
 **/
public final class Version {
    /**
     * 版本号
     */
    private static String version;
    /**
     * 版本名称
     */
    private static String name;
    /**
     * 版本全称
     */
    private static String fullVersion;

    static {
        name = Version.class.getPackage().getImplementationTitle();
        name = Objects.isNull(name) ? "ECMP" : name;
        version = Version.class.getPackage().getImplementationVersion();
        version = Objects.isNull(version) ? "dev" : version;
        fullVersion = name + " " + version;
    }

    private Version() {
    }

    public static String getCurrentVersion() {
        return version;
    }

    public static String getName() {
        return name;
    }

    /**
     * 返回版本字符串
     */
    public static String getCompleteVersionString() {
        return fullVersion;
    }

    @Override
    public String toString() {
        return fullVersion;
    }
}
