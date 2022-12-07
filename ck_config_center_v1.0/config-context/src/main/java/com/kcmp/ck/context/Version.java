package com.kcmp.ck.context;

import java.util.Objects;

/**
 * @author kikock
 * @version 1.0.0
 */
public final class Version {
    private static String version;
    private static String name;
    private static String fullVersion;

    static {
        name = Version.class.getPackage().getImplementationTitle();
        name = Objects.isNull(name) ? "KCMP" : name;
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
     * Returns version string as normally used in print, such as KCMP 1.0.0
     */
    public static String getCompleteVersionString() {
        return fullVersion;
    }

    @Override
    public String toString() {
        return fullVersion;
    }
}
