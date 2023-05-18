package com.kcmp.ck.flow.basic.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kikock
 * 查询员工用户vo
 * @email kikock@qq.com
 **/
public class EmployeeQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页数
     */
    private int page;

    /**
     * 行数
     */
    private int rows;

    /**
     * 需要排除的员工用户id列表
     */
    private List<String> ids;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
