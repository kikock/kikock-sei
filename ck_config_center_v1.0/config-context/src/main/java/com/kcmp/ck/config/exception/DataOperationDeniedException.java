package com.kcmp.ck.config.exception;

/**
 * <strong>实现功能:</strong>.
 * <p>数据操作异常</p>
 *
 * @author kikock
 * @version 1.0.0
 */
public class DataOperationDeniedException extends BaseRuntimeException {

    private static final long serialVersionUID = 4152774583665523431L;

    public DataOperationDeniedException() {
        super("无效数据操作");
    }

    public DataOperationDeniedException(String msg) {
        super(msg);
    }

    public DataOperationDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
