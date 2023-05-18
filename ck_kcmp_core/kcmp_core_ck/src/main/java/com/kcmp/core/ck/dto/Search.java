package com.kcmp.core.ck.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kikock
 * 查询配置
 * @email kikock@qq.com
 **/
public class Search implements Serializable {

    private static final long serialVersionUID = 556148806959809329L;

    public static final String DESC = "desc";

    /**
     * 快速搜索配置
     */
    private Collection<String> quickSearchProperties;
    /**
     * 快速搜索内容
     */
    private String quickSearchValue;
    /**
     * 筛选字段
     */
    private List<SearchFilter> filters;
    /**
     * 排序字段
     */
    private List<SearchOrder> sortOrders;
    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    public Search() {
    }

    public Search(Collection<String> quickSearchProperties, String quickSearchValue) {
        this.quickSearchProperties = quickSearchProperties;
        this.quickSearchValue = quickSearchValue;
    }

    public Search(Collection<String> quickSearchProperties, String quickSearchValue, List<SearchFilter> filters,
                  List<SearchOrder> sortOrders, PageInfo pageInfo) {
        this.quickSearchProperties = quickSearchProperties;
        this.quickSearchValue = quickSearchValue;
        this.filters = filters;
        this.sortOrders = sortOrders;
        this.pageInfo = pageInfo;
    }

    public Search(List<SearchFilter> filters, List<SearchOrder> sortOrders, PageInfo pageInfo) {
        this.filters = filters;
        this.sortOrders = sortOrders;
        this.pageInfo = pageInfo;
    }

    /**
     * 通过快速查询参数构造
     * @param searchParam 快速查询参数
     */
    public Search(QuickSearchParam searchParam) {
        quickSearchValue = searchParam.getQuickSearchValue();
        quickSearchProperties = searchParam.getQuickSearchProperties();
        sortOrders = searchParam.getSortOrders();
        pageInfo = searchParam.getPageInfo();
    }

    /**
     * 通过高级查询参数构造
     * @param searchParam 高级查询参数
     */
    public Search(SearchParam searchParam) {
        filters = searchParam.getFilters();
        sortOrders = searchParam.getSortOrders();
        pageInfo = searchParam.getPageInfo();
    }

    public Collection<String> getQuickSearchProperties() {
        return quickSearchProperties;
    }

    /**
     * 添加快速搜索属性名
     * @param property 属性名
     * @return 返回已添加的快速搜索属性名集合
     */
    public Search addQuickSearchProperty(String property) {
        if (quickSearchProperties == null) {
            quickSearchProperties = new HashSet<String>();
        }
        if (StringUtils.isNotBlank(property)) {
            quickSearchProperties.add(property);
            this.setQuickSearchProperties(quickSearchProperties);
        }
        return this;
    }

    public Search setQuickSearchProperties(Collection<String> quickSearchProperties) {
        this.quickSearchProperties = quickSearchProperties;
        return this;
    }

    public String getQuickSearchValue() {
        return quickSearchValue;
    }

    public Search setQuickSearchValue(String quickSearchValue) {
        this.quickSearchValue = quickSearchValue;
        return this;
    }

    public List<SearchFilter> getFilters() {
        return filters;
    }

    public Search setFilters(List<SearchFilter> filters) {
        this.filters = filters;
        return this;
    }

    public Search addFilter(SearchFilter searchFilter) {
        if (searchFilter != null) {
            if (filters == null) {
                filters = new ArrayList<SearchFilter>();
            }
            filters.add(searchFilter);
        }
        return this;
    }

    public List<SearchOrder> getSortOrders() {
        return sortOrders;
    }

    public Search setSortOrders(List<SearchOrder> sortOrders) {
        this.sortOrders = sortOrders;
        return this;
    }

    public Search addSortOrder(SearchOrder sortOrder) {
        if (sortOrder != null) {
            if (sortOrders == null) {
                sortOrders = new LinkedList<SearchOrder>();
            }
            sortOrders.add(sortOrder);
        }
        return this;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public void clearAll() {
        if (Objects.nonNull(this.quickSearchProperties)) {
            this.quickSearchProperties.clear();
        }
        this.quickSearchValue = null;
        if (Objects.nonNull(this.filters)) {
            this.filters.clear();
        }
        if (Objects.nonNull(this.sortOrders)) {
            this.sortOrders.clear();
        }
        this.pageInfo = null;
    }

    public static Search createSearch() {
        return new Search();
    }
}
