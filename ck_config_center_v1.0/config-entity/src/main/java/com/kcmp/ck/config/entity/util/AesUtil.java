package com.kcmp.ck.config.entity.util;


import com.kcmp.ck.config.util.EncodeUtil;

/**
 * Created by kikock
 * Aes加密
 *
 * @author kikock
 * @email kikock@qq.com
 **/
public class AesUtil {
    /**
     * 默认的密钥
     */
    private final static String AES_KEY = "uafiHQsXrZRDBkrRSCumZNKVtEMwUpURPdHH";

    /**
     * 用平台默认的密钥加密
     *
     * @param data 待加密数据
     * @return 加密后的数据
     */
    public static String encrypt(String data) {
        try {
            return EncodeUtil.aesEncrypt(data, AES_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 用平台默认的密钥解密
     *
     * @param data 待解密数据
     * @return 解密后的数据
     */
    public static String decrypt(String data) {
        try {
            return EncodeUtil.aesDecrypt(data, AES_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static void main(String[] args) {
        String s = "1234";
        String d = encrypt(s);
        System.out.println(d);
        System.out.println(decrypt(d));
    }
}

