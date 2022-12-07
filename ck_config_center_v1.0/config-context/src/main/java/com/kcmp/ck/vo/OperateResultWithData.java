package com.kcmp.ck.vo;

import java.io.Serializable;

/**
 * <strong>实现功能：</strong>
 * <p>
 * 定义全局操作状态对象
 * 要求所有对外服务及web传输都以该对象输出
 * </p>
 *
 * @param <T> 泛型限定
 * @author kikock
 * @version 1.0.0
 */
public final class OperateResultWithData<T> extends BaseResult implements Serializable {
    private static final long serialVersionUID = -988309204353553239L;

    /**
     * 数据
     */
    private T data;

    private OperateResultWithData() {
    }

    /**
     * @param status  操作状态
     * @param key     多语言key
     * @param msgArgs 多语言参数
     */
    protected OperateResultWithData(StatusEnum status, String key, Object... msgArgs) {
        super(status, key, msgArgs);
    }

    /**
     * @param status  操作状态
     * @param data    数据
     * @param key     多语言key
     * @param msgArgs 多语言参数
     */
    private OperateResultWithData(StatusEnum status, T data, String key, Object... msgArgs) {
        super(status, key, msgArgs);
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
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
     * 获取数据
     *
     * @return 返回数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据
     *
     * @param data 数据
     * @return 返回当前对象
     */
    public OperateResultWithData<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 成功
     *
     * @return 返回操作对象
     */
    public OperateResultWithData<T> succeed() {
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
    public OperateResultWithData<T> succeed(String key, Object... msgArgs) {
        return new OperateResultWithData<T>(StatusEnum.SUCCESS, key, msgArgs);
    }

    /**
     * 失败
     *
     * @param key     多语言key
     * @param msgArgs 多语言参数
     * @return 返回操作对象
     */
    public OperateResultWithData<T> fail(String key, Object... msgArgs) {
        return new OperateResultWithData<T>(StatusEnum.FAILURE, key, msgArgs);
    }

    /**
     * 警告
     *
     * @param key     多语言key
     * @param msgArgs 多语言参数
     * @return 返回操作对象
     */
    public OperateResultWithData<T> warn(String key, Object... msgArgs) {
        return new OperateResultWithData<T>(StatusEnum.WARNING, key, msgArgs);
    }

    @Override
    public String toString() {
        return "OperateResultWithData{"
                + "status=" + status
                //                 +", key=" + key
                + ", message=" + message
                + ", data=" + data
                + "}";
    }

    /**
     * @param <Data> 泛型限定
     * @return 返回一个成功的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationSuccess() {
        return new OperateResultWithData<>(StatusEnum.SUCCESS, "kcmp_context_00001");
    }

    /**
     * @param key    多语言key
     * @param <Data> 泛型限定
     * @return 返回一个成功的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationSuccess(String key) {
        return new OperateResultWithData<>(StatusEnum.SUCCESS, key);
    }

    /**
     * @param key     多语言key
     * @param msgArgs 多语言填充参数
     * @param <Data>  泛型限定
     * @return 返回一个成功的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationSuccess(String key, Object... msgArgs) {
        return new OperateResultWithData<>(StatusEnum.SUCCESS, key, msgArgs);
    }

    /**
     * @param data   数据对象
     * @param <Data> 泛型限定
     * @return 返回一个成功的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationSuccessWithData(Data data) {
        return new OperateResultWithData<>(StatusEnum.SUCCESS, data, "kcmp_context_00001");
    }

    /**
     * @param data    数据对象
     * @param key     多语言key
     * @param msgArgs 多语言填充参数
     * @param <Data>  泛型限定
     * @return 返回一个成功的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationSuccessWithData(Data data, String key, Object... msgArgs) {
        return new OperateResultWithData<>(StatusEnum.SUCCESS, data, key, msgArgs);
    }

    /**
     * @param key    多语言key
     * @param <Data> 泛型限定
     * @return 返回一个失败的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationFailure(String key) {
        return new OperateResultWithData<>(StatusEnum.FAILURE, key);
    }

    /**
     * @param key     多语言key
     * @param msgArgs 多语言填充参数
     * @param <Data>  泛型限定
     * @return 返回一个失败的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationFailure(String key, Object... msgArgs) {
        return new OperateResultWithData<>(StatusEnum.FAILURE, key, msgArgs);
    }

    /**
     * @param key     多语言key
     * @param msgArgs 多语言填充参数
     * @param data    数据对象
     * @param <Data>  泛型限定
     * @return 返回一个失败的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationFailureWithData(Data data, String key, Object... msgArgs) {
        return new OperateResultWithData<>(StatusEnum.FAILURE, data, key, msgArgs);
    }

    /**
     * @param otherResult 操作状态对象
     * @param <Data>      泛型限定
     * @return 返回一个失败的操作状态对象
     */
    @SuppressWarnings("unchecked")
    public static <Data> OperateResultWithData<Data> operationFailureWithData(OperateResultWithData otherResult) {
        OperateResultWithData operateResultWithData = new OperateResultWithData(StatusEnum.FAILURE, (Data) null, null);
        operateResultWithData.setMessage(otherResult.getMessage());
        return operateResultWithData;
    }

    /**
     * @param key    多语言key
     * @param <Data> 泛型限定
     * @return 返回一个警告的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationWarning(String key) {
        return new OperateResultWithData<>(StatusEnum.WARNING, key);
    }

    /**
     * @param key     多语言key
     * @param msgArgs 多语言填充参数
     * @param <Data>  泛型限定
     * @return 返回一个警告的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationWarning(String key, Object... msgArgs) {
        return new OperateResultWithData<>(StatusEnum.WARNING, key, msgArgs);
    }

    /**
     * @param key     多语言key
     * @param data    数据对象
     * @param msgArgs 多语言填充参数
     * @param <Data>  泛型限定
     * @return 返回一个警告的操作状态对象
     */
    public static <Data> OperateResultWithData<Data> operationWarningWithData(Data data, String key, Object... msgArgs) {
        return new OperateResultWithData<>(StatusEnum.WARNING, data, key, msgArgs);
    }

    /**
     * @param resultWithData 操作结果
     * @return 返回withData的结果
     */
    public static OperateResult converterNoneData(OperateResultWithData<?> resultWithData) {
        return new OperateResult(resultWithData.status, resultWithData.getMessage());
    }
}
