package com.kcmp.core.ck.ws;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kikock
 * 消息对象
 * @email kikock@qq.com
 **/
public class MessageData {
    public static final String ID_KEY = MessageData.class.getName() + ".ID";
    private static final AtomicInteger ID = new AtomicInteger();

    private final String heading;
    private final StringBuilder address;
    private final StringBuilder contentType;
    private final StringBuilder encoding;
    private final StringBuilder httpMethod;
    private final StringBuilder header;
    private final StringBuilder message;
    private final StringBuilder payload;
    private final StringBuilder responseCode;
    private final String id;

    public MessageData(String h, String i) {
        heading = h;
        id = i;

        contentType = new StringBuilder();
        address = new StringBuilder();
        encoding = new StringBuilder();
        httpMethod = new StringBuilder();
        header = new StringBuilder();
        message = new StringBuilder();
        payload = new StringBuilder();
        responseCode = new StringBuilder();
    }

    public String getId() {
        return id;
    }

    public static String nextId() {
        return Integer.toString(ID.incrementAndGet());
    }


    public StringBuilder getAddress() {
        return address;
    }

    public StringBuilder getEncoding() {
        return encoding;
    }

    public StringBuilder getHeader() {
        return header;
    }

    public StringBuilder getHttpMethod() {
        return httpMethod;
    }

    public StringBuilder getContentType() {
        return contentType;
    }

    public StringBuilder getMessage() {
        return message;
    }

    public StringBuilder getPayload() {
        return payload;
    }

    public StringBuilder getResponseCode() {
        return responseCode;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(heading);
        buffer.append("\nID: ").append(id);
        if (address.length() > 0) {
            buffer.append("\nAddress: ");
            buffer.append(address);
        }
        if (responseCode.length() > 0) {
            buffer.append("\nResponse-Code: ");
            buffer.append(responseCode);
        }
        if (encoding.length() > 0) {
            buffer.append("\nEncoding: ");
            buffer.append(encoding);
        }
        if (httpMethod.length() > 0) {
            buffer.append("\nHttp-Method: ");
            buffer.append(httpMethod);
        }
        buffer.append("\nContent-Type: ");
        buffer.append(contentType);
        buffer.append("\nHeaders: ");
        buffer.append(header);
        if (message.length() > 0) {
            buffer.append("\nMessages: ");
            buffer.append(message);
        }
        if (payload.length() > 0) {
            buffer.append("\nPayload: ");
            buffer.append(payload);
        }
        buffer.append("\n\r");
        return buffer.toString();
    }
}
