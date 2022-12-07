package com.kcmp.ck.config.util;

import com.kcmp.ck.context.ContextUtil;
import com.kcmp.ck.vo.SessionUser;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author kikock
 * @version 1.0.0
 */
public class OkHttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpClient.class);

    /**
     * get请求
     */
    public static <T> OkhttpResult<T> get(OkHttpParam restParam, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();

        if (restParam.getApiPath() != null && restParam.getApiPath().length() > 0) {
            url = url + restParam.getApiPath();
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return exec(restParam, request, tClass);
    }

    /**
     * POST请求json数据
     */
    public static <T> OkhttpResult<T> post(OkHttpParam restParam, String reqJsonData, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();

        if (restParam.getApiPath() != null && restParam.getApiPath().length() > 0) {
            url = url + restParam.getApiPath();
        }
        RequestBody body = RequestBody.create(restParam.getMediaType(), reqJsonData);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return exec(restParam, request, tClass);
    }

    /**
     * POST请求map数据
     */
    public static <T> OkhttpResult<T> post(OkHttpParam restParam, Map<String, Object> parms, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();

        if (restParam.getApiPath() != null && restParam.getApiPath().length() > 0) {
            url = url + restParam.getApiPath();
        }
        FormBody.Builder builder = new FormBody.Builder();

        if (parms != null) {
            for (Map.Entry<String, Object> entry : parms.entrySet()) {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        FormBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return exec(restParam, request, tClass);
    }

    /**
     * 返回值封装成对象
     */
    private static <T> OkhttpResult<T> exec(OkHttpParam restParam, Request request, Class<T> tClass) throws Exception {
        OkhttpResult clientResult = exec(restParam, request);
        String result = clientResult.getResult();
        int status = clientResult.getStatus();

        T t = null;
        if (status == 200) {
            if (result != null && !"".equals(result)) {
                t = JsonUtils.fromJson(result, tClass);
            }
        }
        return new OkhttpResult<>(clientResult.getStatus(), result, t);
    }

    /**
     * 执行方法
     */
    private static OkhttpResult exec(OkHttpParam restParam, Request request) throws Exception {
        OkhttpResult result = null;

        okhttp3.OkHttpClient client = null;
        ResponseBody responseBody = null;
        try {
            client = new okhttp3.OkHttpClient();

            client.newBuilder()
                    .connectTimeout(restParam.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(restParam.getReadTimeout(), TimeUnit.MILLISECONDS)
                    .writeTimeout(restParam.getWriteTimeout(), TimeUnit.MILLISECONDS);

            okhttp3.Request.Builder builder = request.newBuilder();
            builder.addHeader("Content-Type", "application/json; charset=utf-8");
            //            builder.addHeader("Accept", "application/json; charset=utf-8");

            SessionUser sessionUser = ContextUtil.getSessionUser();
            //从运行环境上下文中获取当前session id
            String token = sessionUser.getAccessToken();
            LOGGER.debug("request AccessToken {}", token);
            if (token != null) {
                builder.addHeader(ContextUtil.AUTHORIZATION_KEY, token);
            }
            String traceId = MDC.get("traceId");
            if (traceId == null || traceId.length() == 0) {
                traceId = IdGenerator.uuid2();
            }
            LOGGER.debug("request traceId {}", traceId);
            builder.addHeader("traceId", traceId);
            LOGGER.debug("request uri {}", request.url());
            request = builder.build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                responseBody = response.body();
                if (responseBody != null) {
                    String responseString = responseBody.string();

                    result = new OkhttpResult<>(response.code(), responseString, null);
                }
            } else {
                throw new Exception(response.message());
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
            if (client != null) {
                client.dispatcher().executorService().shutdown();   //清除并关闭线程池
                client.connectionPool().evictAll();                 //清除并关闭连接池
                try {
                    if (client.cache() != null) {
                        client.cache().close();                         //清除cache
                    }
                } catch (IOException e) {
                    throw new Exception(e.getMessage(), e);
                }
            }
        }
        return result;
    }
}
