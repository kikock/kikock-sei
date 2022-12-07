package com.kcmp.core.ck.entity;

/**
 * Created by kikock
 * 逻辑（软）删除特征接口，实体实现该接口平台默认将dao层的delete方法以软删除方式实现
 * @email kikock@qq.com
 **/
public interface ISoftDelete {

    String DELETED = "deleted";

    /**
     * 建议以当前时间戳作为deleted的值
     * 以非0的值为删除做判断
     * @return 等于0则可用，非0则删除
     */
    Long getDeleted();

    /**
     * 当前时间戳作为deleted的值
     * @param deleted 当前时间戳
     */
    void setDeleted(Long deleted);
}
