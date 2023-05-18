package com.kcmp.core.ck.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kikock
 * 分页查询参数
 * @email kikock@qq.com
 **/
public class QueryParam implements Serializable {
    private static final long serialVersionUID = 612011703347762992L;
    /**
     * 排序字段
     */
    private List<SearchOrder> sortOrders;
    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    public List<SearchOrder> getSortOrders() {
        return sortOrders;
    }

    public void setSortOrders(List<SearchOrder> sortOrders) {
        this.sortOrders = sortOrders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
