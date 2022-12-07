package com.kcmp.ck.config.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 实现功能：
 *
 * @author kikock
 * @version 1.0.0
 */
public class Signature {

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(HEX[(aByte >> 4) & 0x0f]);
            buf.append(HEX[aByte & 0x0f]);
        }
        return buf.toString();
    }

    public static String sign(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        String key = "noMobile";
        String userLoginID = "gw00174324";
        String stamp = "1558430160975";
        String token = "ae74344ebb4e8235da400b9fd1bad221f0fd3c06";
        String sha1Token = sign(key + userLoginID + stamp);
        System.out.println(token.equals(sha1Token));

        String account = "CORP\\test001";
        int idx = account.indexOf("\\");
        account = account.substring(idx + 1);
        System.out.println(account);
    }
}
