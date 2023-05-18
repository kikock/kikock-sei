package com.kcmp.ck.flow.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 请求执行人时候的参数VO
 * @email kikock@qq.com
 **/
public class RequestExecutorsVo implements Serializable {

       private  String userType; //执行人类型参数
       private  String ids;      //选择参数

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
