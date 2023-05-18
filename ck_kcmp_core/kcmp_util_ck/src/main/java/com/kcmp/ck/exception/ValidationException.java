package com.kcmp.ck.exception;

/**
 * Created by kikock
 * 业务逻辑校验异常
 * @email kikock@qq.com
 **/
public class ValidationException extends BaseRuntimeException {

    private static final long serialVersionUID = 176535959241013839L;

    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ValidationException(String message) {
        super(message);
    }
}
