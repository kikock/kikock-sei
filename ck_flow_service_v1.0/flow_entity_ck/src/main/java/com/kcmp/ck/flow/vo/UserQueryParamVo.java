package com.kcmp.ck.flow.vo;


import com.kcmp.core.ck.dto.QuickSearchParam;

/**
 * Created by kikock
 * 企业员工用户查询参数
 * @email kikock@qq.com
 **/
public class UserQueryParamVo extends QuickSearchParam {

    /**
     * 是否包含组织机构子节点
     */
    private Boolean includeSubNode = Boolean.FALSE;

    /**
     * 组织机构Id
     */
    private String organizationId;

    /**
     * 岗位id
     */
    private String positionId;

    public Boolean getIncludeSubNode() {
        return includeSubNode;
    }

    public void setIncludeSubNode(Boolean includeSubNode) {
        this.includeSubNode = includeSubNode;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

}
