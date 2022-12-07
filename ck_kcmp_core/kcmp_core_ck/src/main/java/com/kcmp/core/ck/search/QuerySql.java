package com.kcmp.core.ck.search;

import java.io.Serializable;

/**
 * Created by kikock
 * 查询的SQL类
 * @email kikock@qq.com
 **/
public class QuerySql implements Serializable {
    private static final long serialVersionUID = 843710477794460461L;
    /**
     * 查询部分
     */
    private String select;
    /**
     * 获取数据量的查询部分
     */
    private String countSelect;
    /**
     * 查询语句的FROM和WHERE部分
     */
    private String fromAndWhere;
    /**
     * 排序部分
     */
    private String orderBy;

    /**
     * 构造函数
     * @param select 查询语句的查询部分
     * @param fromAndWhere 查询语句的FROM和WHERE部分
     */
    public QuerySql(String select, String fromAndWhere) {
        this.select = select;
        this.fromAndWhere = fromAndWhere;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getCountSelect() {
        return countSelect;
    }

    public void setCountSelect(String countSelect) {
        this.countSelect = countSelect;
    }

    public String getFromAndWhere() {
        return fromAndWhere;
    }

    public void setFromAndWhere(String fromAndWhere) {
        this.fromAndWhere = fromAndWhere;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
