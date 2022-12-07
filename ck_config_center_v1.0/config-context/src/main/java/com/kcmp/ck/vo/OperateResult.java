package com.kcmp.ck.vo;

import java.io.Serializable;

/**
 * <strong>实现功能：</strong>
 * <p>
 * 定义全局操作状态对象
 * 要求所有对外服务及web传输都以该对象输出
 * </p>
 *
 * @author kikock
 * @version 1.0.0
 */
public final class OperateResult extends BaseResult implements Serializable {
    private static final long serialVersionUID = 652732203000606895L;

    /**
     *
     */
    private OperateResult() {
    }

    //    /**
    //     * @param status  操作状态
    //     * @param message 消息
    //     */
    //    protected OperateResult(StatusEnum status, String message) {
    //        super(status, message);
    //    }

    /**
     * @param status  操作状态
     * @param key     多语言key
     * @param msgArgs 多语言参数
     */
    protected OperateResult(StatusEnum status, String key, Object... msgArgs) {
        super(status, key, msgArgs);
    }

    /**
     * 是否成功
     *
     * @return 返回true，则表示操作成功，反之失败
     */
    public boolean successful() {
        return StatusEnum.SUCCESS.equals(status);
    }

    /**
     * 是否未成功
     *
     * @return 返回true，则表示操作失败，反之成功
     */
    public boolean notSuccessful() {
        return !successful();
    }

    /**
     * 成功
     *
     * @return 返回操作对象
     */
    public OperateResult succeed() {
        this.status = StatusEnum.SUCCESS;
        return this;
    }

    /**
     * 成功
     *
     * @param key     多语言key
     * @param msgArgs 多语言参数
     * @return 返回操作对象
     */
    public OperateResult succeed(String key, Object... msgArgs) {
        return new OperateResult(StatusEnum.SUCCESS, key, msgArgs);
    }

    /**
     * 失败
     *
     * @param key     多语言key
     * @param msgArgs 多语言参数
     * @return 返回操作对象
     */
    public OperateResult fail(String key, Object... msgArgs) {
        return new OperateResult(StatusEnum.FAILURE, key, msgArgs);
    }

    /**
     * 警告
     *
     * @param key     多语言key
     * @param msgArgs 多语言参数
     * @return 返回操作对象
     */
    public OperateResult warn(String key, Object... msgArgs) {
        return new OperateResult(StatusEnum.WARNING, key, msgArgs);
    }

    @Override
    public String toString() {
        return "OperateResult{"
                + "status=" + status
                //                 +", key=" + key + ""
                + ", message=" + message
                + "}";
    }


    /**
     * @return 返回一个成功的操作状态对象
     */
    public static OperateResult operationSuccess() {
        return new OperateResult(StatusEnum.SUCCESS, "kcmp_context_00001");
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
        OperateResultWithData<T> resultWithData = new OperateResultWithData<T>(result.status, result.getMessage());
        resultWithData.setData(t);
        return resultWithData;
    }
}
