package com.kcmp.ck.exception;

/**
 * Created by kikock
 * 平台异常基类
 * @email kikock@qq.com
 **/
public class KcmpException extends BaseRuntimeException {

    private static final long serialVersionUID = 299216441742097674L;

    public KcmpException(String msg) {
        super(msg);
    }

    public KcmpException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
