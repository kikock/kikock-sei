package com.kcmp.ck.config.exception;

/**
 * <strong>实现功能:</strong>.
 * <p>KCMP平台异常基类</p>
 *
 * @author kikock
 * @version 1.0.0
 */
public class KcmpException extends BaseRuntimeException {

    private static final long serialVersionUID = -2896111728089420354L;

    public KcmpException(String msg) {
        super(msg);
    }

    public KcmpException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
