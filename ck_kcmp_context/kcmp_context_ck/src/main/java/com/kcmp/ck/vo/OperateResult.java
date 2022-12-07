package com.kcmp.ck.vo;

import java.io.Serializable;

/**
 * Created by kikock
 * 全局返回对象
 * @email kikock@qq.com
 **/
public final class OperateResult extends ResponseData<Object> implements Serializable {
    private static final long serialVersionUID = 737548610884255366L;

    private OperateResult() {
    }

    /**
     * @param status  操作状态
     * @param key     多语言key
     * @param msgArgs 多语言参数
     */
    protected OperateResult(StatusEnum status, String key, Object... msgArgs) {
        super(status, key, msgArgs);
    }

    /**
     * 成功
     * @return 返回操作对象
     */
    @Override
    public OperateResult succeed() {
        this.success = Boolean.TRUE;
        this.status = StatusEnum.SUCCESS;
        return this;
    }

    /**
     * 成功
     * @param key     多语言key
     * @param msgArgs 多语言参数
     * @return 返回操作对象
     */
    @Override
    public OperateResult succeed(String key, Object... msgArgs) {
        this.success = Boolean.TRUE;
        this.status = StatusEnum.SUCCESS;
        this.message = message(key, msgArgs);
        return this;
    }

    /**
     * 失败
     * @param key     多语言key
     * @param msgArgs 多语言参数
     * @return 返回操作对象
     */
    @Override
    public OperateResult fail(String key, Object... msgArgs) {
        this.success = Boolean.FALSE;
        this.status = StatusEnum.FAILURE;
        this.message = message(key, msgArgs);
        return this;
    }

    /**
     * 警告
     * @param key     多语言key
     * @param msgArgs 多语言参数
     * @return 返回操作对象
     */
    @Override
    public OperateResult warn(String key, Object... msgArgs) {
        this.success = Boolean.FALSE;
        this.status = StatusEnum.WARNING;
        this.message = message(key, msgArgs);
        return this;
    }

    @Override
    public String toString() {
        return "OperateResult{"
                + "status=" + status
                + ", message=" + message
                + "}";
    }

    /**
     * 返回一个成功的操作状态对象
     */
    public static OperateResult operationSuccess() {
        return new OperateResult(StatusEnum.SUCCESS, "ecmp_context_00001");
    }

    /**
     * @param key 多语言key
     * @return 返回一个成功的操作状态对象
     */
    public static OperateResult operationSuccess(String key) {
        return new OperateResult(StatusEnum.SUCCESS, key);
    }

    /**
     * @param key     多语言key
     * @param msgArgs 多语言填充参数
     * @return 返回一个成功的操作状态对象
     */
    public static OperateResult operationSuccess(String key, Object... msgArgs) {
        return new OperateResult(StatusEnum.SUCCESS, key, msgArgs);
    }

    /**
     * @param key 多语言key
     * @return 返回一个失败的操作状态对象
     */
    public static OperateResult operationFailure(String key) {
        return new OperateResult(StatusEnum.FAILURE, key);
    }

    /**
     * @param key     多语言key
     * @param msgArgs 多语言填充参数
     * @return 返回一个失败的操作状态对象
     */
    public static OperateResult operationFailure(String key, Object... msgArgs) {
        return new OperateResult(StatusEnum.FAILURE, key, msgArgs);
    }

    /**
     * @param key 多语言key
     * @return 返回一个警告的操作状态对象
     */
    public static OperateResult operationWarning(String key) {
        return new OperateResult(StatusEnum.WARNING, key);
    }

    /**
     * @param key     多语言key
     * @param msgArgs 多语言填充参数
     * @return 返回一个警告的操作状态对象
     */
    public static OperateResult operationWarning(String key, Object... msgArgs) {
        return new OperateResult(StatusEnum.WARNING, key, msgArgs);
    }

    /**
     * @param result 操作结果
     * @param t      类型
     * @param <T>    泛型
     * @return 返回withData的结果
     */
    public static <T extends Serializable> OperateResultWithData<T> converterWithData(OperateResult result, T t) {
        return new OperateResultWithData<>(result.status, t, result.getMessage());
    }
}
