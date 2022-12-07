package com.kcmp.ck.config.exception;

/**
 * <strong>实现功能:</strong>.
 * <p>服务层异常</p>
 *
 * @author kikock
 * @version 1.0.0
 */
public class ServiceException extends BaseRuntimeException {

    private static final long serialVersionUID = -344443052461115514L;

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
