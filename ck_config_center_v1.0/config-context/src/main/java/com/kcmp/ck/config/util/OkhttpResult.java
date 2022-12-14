package com.kcmp.ck.config.util;

/**
 * @author kikock
 * @version 1.0.0
 */
public class OkhttpResult<T> {

    private int status;
    private String result;
    private T resultObject;

    public OkhttpResult() {
    }

    public OkhttpResult(int status, String result, T resultObject) {
        this.status = status;
        this.result = result;
        this.resultObject = resultObject;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public T getResultObject() {
        return resultObject;
    }

    public void setResultObject(T resultObject) {
        this.resultObject = resultObject;
    }
}
