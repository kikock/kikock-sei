package com.kcmp.ck.exception;

/**
 * Created by kikock
 * 平台异常基类
 * @email kikock@qq.com
 **/
public class EcmpException extends BaseRuntimeException {

    private static final long serialVersionUID = 299216441742097674L;

    public EcmpException(String msg) {
        super(msg);
    }

    public EcmpException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
