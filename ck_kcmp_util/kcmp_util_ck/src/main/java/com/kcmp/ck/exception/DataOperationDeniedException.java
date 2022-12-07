package com.kcmp.ck.exception;

/**
 * Created by kikock
 * 数据操作异常
 * @email kikock@qq.com
 **/
public class DataOperationDeniedException extends BaseRuntimeException {

    private static final long serialVersionUID = 972598374208646958L;

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
