package com.kcmp.ck.config.exception;

/**
 * <strong>实现功能:</strong>.
 * <p>运行期异常</p>
 *
 * @author kikock
 * @version 1.0.0
 */
public abstract class BaseRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -23347847086757165L;

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
