package com.kcmp.test.ck.dao.ext;

import com.kcmp.test.ck.entity.AppModule;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by kikock
 * 应用模块扩展dao
 * @author kikock
 * @email kikock@qq.com
 **/
@Repository
public interface AppModuleExtDao {

    /**
     * 根据编码列表查询应用模块列表
     * @param codeList  编码列表
     * @return
     */
    @Transactional(readOnly = true)
    List<AppModule> findByCodes(List<String> codeList);
}
