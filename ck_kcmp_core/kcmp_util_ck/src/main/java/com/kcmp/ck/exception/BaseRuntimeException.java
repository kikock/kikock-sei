package com.kcmp.ck.exception;

/**
 * Created by kikock
 * 运行期异常
 * @email kikock@qq.com
 **/
public abstract class BaseRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 799920480854245895L;

    private String errorCode;

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
