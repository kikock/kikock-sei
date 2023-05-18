package com.kcmp.ck.exception;

/**
 * Created by kikock
 * 数据访问权限异常
 * @email kikock@qq.com
 **/
public class DataAccessDeniedException extends BaseRuntimeException {

    private static final long serialVersionUID = 143549118595193534L;

    public DataAccessDeniedException() {
        super("无权数据访问");
    }

    public DataAccessDeniedException(String msg) {
        super(msg);
    }

    public DataAccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
