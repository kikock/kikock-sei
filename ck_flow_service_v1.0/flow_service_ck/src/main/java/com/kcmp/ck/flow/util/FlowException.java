package com.kcmp.ck.flow.util;

/**
 * Created by kikock
 * 自定义异常
 * @author kikock
 * @email kikock@qq.com
 **/
public class FlowException extends  RuntimeException {
    public FlowException() {
        super();
    }
    public FlowException(String message) {
        super(message);
    }

    public FlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
