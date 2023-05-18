package com.kcmp.ck.exception;

/**
 * Created by kikock
 * 重复提交异常
 * @email kikock@qq.com
 **/
public class DuplicateTokenException extends BaseRuntimeException {

    private static final long serialVersionUID = 323557405510207596L;

    public DuplicateTokenException(String msg) {
        super(msg);
    }

    public DuplicateTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
