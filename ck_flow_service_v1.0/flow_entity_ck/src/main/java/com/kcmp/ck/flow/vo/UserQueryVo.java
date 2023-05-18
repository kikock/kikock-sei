package com.kcmp.ck.flow.vo;


import com.kcmp.core.ck.dto.PageInfo;

import java.io.Serializable;

/**
 * Created by kikock
 * 企业员工用户查询参数
 * @email kikock@qq.com
 **/
public class UserQueryVo implements Serializable {

    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    /**
     * 组织机构ID
     */
    private String organizationId;


    /**
     * 是否包含组织机构子节点
     */
    private Boolean includeSubNode = Boolean.TRUE;


    /**
     * 快速查询值
     */
    private String quickSearchValue;


    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Boolean getIncludeSubNode() {
        return includeSubNode;
    }

    public void setIncludeSubNode(Boolean includeSubNode) {
        this.includeSubNode = includeSubNode;
    }

    public String getQuickSearchValue() {
        return quickSearchValue;
    }

    public void setQuickSearchValue(String quickSearchValue) {
        this.quickSearchValue = quickSearchValue;
    }
}
