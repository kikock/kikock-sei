package com.kcmp.core.ck.search;

/**
 * Created by kikock
 * 快速查询参数
 * @email kikock@qq.com
 **/
public class QuickQueryParam extends QueryParam {
    private static final long serialVersionUID = 193922441607341257L;
    /**
     * 快速搜索关键字
     */
    private String quickSearchValue;

    public String getQuickSearchValue() {
        return quickSearchValue;
    }

    public void setQuickSearchValue(String quickSearchValue) {
        this.quickSearchValue = quickSearchValue;
    }
}
