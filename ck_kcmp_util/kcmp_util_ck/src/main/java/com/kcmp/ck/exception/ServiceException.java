package com.kcmp.ck.exception;

/**
 * Created by kikock
 * 服务层异常
 * @email kikock@qq.com
 **/
public class ServiceException extends BaseRuntimeException {

    private static final long serialVersionUID = 448182377642993537L;

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
