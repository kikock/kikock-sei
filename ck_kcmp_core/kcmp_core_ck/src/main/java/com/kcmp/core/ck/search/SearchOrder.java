package com.kcmp.core.ck.search;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by kikock
 * 查询排序配置
 * @email kikock@qq.com
 **/
public class SearchOrder implements Serializable {

    private static final long serialVersionUID = 808430104362615463L;

    /**
     * 排序字段
     */
    private String property;
    /**
     * 排序方向
     */
    private Direction direction;

    public SearchOrder() {

    }

    public SearchOrder(String property) {
        this.property = property;
        this.direction = Direction.ASC;
    }

    public SearchOrder(String property, Direction direction) {
        this.property = property;
        this.direction = direction;
    }

    public static SearchOrder asc(String property) {
        return new SearchOrder(property, Direction.ASC);
    }

    public static SearchOrder desc(String property) {
        return new SearchOrder(property, Direction.DESC);
    }

    public String getProperty() {
        return property;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * 排序方向枚举
     */
    public enum Direction {

        ASC, DESC;

        /**
         * 返回排序方向
         * @param value
         * @return
         */
        public static Direction fromString(String value) {
            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
            }
        }
    }
}
