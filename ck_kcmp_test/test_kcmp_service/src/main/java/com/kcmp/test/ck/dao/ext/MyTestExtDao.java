package com.kcmp.test.ck.dao.ext;

import com.kcmp.test.ck.entity.MyTest;

import java.util.List;

/**
 * Created by kikock
 * 扩展dao
 * @author kikock
 * @email kikock@qq.com
 **/
public interface MyTestExtDao {
    /**
     * 查询测试
     * @param codeList  代码列表
     * @return
     */
    List<MyTest> findByCodes(List<String> codeList);
}
