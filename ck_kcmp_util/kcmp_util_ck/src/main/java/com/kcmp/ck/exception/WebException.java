package com.kcmp.ck.exception;

/**
 * Created by kikock
 * web层异常
 * @email kikock@qq.com
 **/
public class WebException extends BaseRuntimeException {

    private static final long serialVersionUID = 754391399388160893L;

    public WebException(String msg) {
        super(msg);
    }

    public WebException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
