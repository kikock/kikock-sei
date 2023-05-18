package com.kcmp.core.ck.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by kikock
 * 快速分页查询参数
 * @email kikock@qq.com
 **/
public class QuickSearchParam implements Serializable {
    private static final long serialVersionUID = 621467723853247203L;
    /**
     * 快速搜索关键字
     */
    private String quickSearchValue;
    /**
     * 快速搜索属性清单
     */
    private Collection<String> quickSearchProperties;
    /**
     * 排序字段
     */
    private List<SearchOrder> sortOrders;
    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    public String getQuickSearchValue() {
        return quickSearchValue;
    }

    public void setQuickSearchValue(String quickSearchValue) {
        this.quickSearchValue = quickSearchValue;
    }

    public Collection<String> getQuickSearchProperties() {
        return quickSearchProperties;
    }

    public void setQuickSearchProperties(Collection<String> quickSearchProperties) {
        this.quickSearchProperties = quickSearchProperties;
    }

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
