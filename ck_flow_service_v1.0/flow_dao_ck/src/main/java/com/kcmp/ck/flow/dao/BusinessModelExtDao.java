package com.kcmp.ck.flow.dao;

import com.kcmp.ck.flow.entity.BusinessModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kikock
 * 业务实体扩展dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface BusinessModelExtDao {

    /**
     * 根据编码列表查询业务实体列表
     * @param codeList  关联的应用模块编码列表
     * @return
     */
    List<BusinessModel> findByAppModuleCodes(List<String> codeList);
}
