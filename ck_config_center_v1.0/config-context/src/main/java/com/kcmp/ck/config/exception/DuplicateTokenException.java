package com.kcmp.ck.config.exception;

/**
 * <strong>实现功能:</strong>.
 * <p>重复提交异常</p>
 *
 * @author kikock
 * @version 1.0.0
 */
public class DuplicateTokenException extends BaseRuntimeException {

    private static final long serialVersionUID = -2896111728089420354L;

    public DuplicateTokenException(String msg) {
        super(msg);
    }

    public DuplicateTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
