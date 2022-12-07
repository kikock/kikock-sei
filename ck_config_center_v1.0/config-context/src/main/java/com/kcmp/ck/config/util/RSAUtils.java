package com.kcmp.ck.config.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加解密工具类
 *
 * @author kikock
 * @version 1.0.0
 */
public class RSAUtils {

    public static final String PUBLIC_KEY = "public_key";
    public static final String PRIVATE_KEY = "private_key";

    /**
     * 生成公钥和私钥
     *
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static Map<String, String> getKeys() {
        Map<String, String> map = new HashMap<String, String>();
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        //生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put(PUBLIC_KEY, getPublicKey2Str(publicKey));
        map.put(PRIVATE_KEY, getPrivateKey2Str(privateKey));
        return map;
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return RSA公钥
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return RSA私钥
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 返回加密子串
     * @throws Exception 加密异常
     */
    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        RSAPublicKey key = (RSAPublicKey) getPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return encrypt(data, key, cipher);

    }

    /**
     * 私钥解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return 解密子串
     * @throws Exception 解密异常
     */
    public static String decryptByPrivateKey(String data, String privateKey) throws Exception {
        RSAPrivateKey key = (RSAPrivateKey) getPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return decrypt(data, key, cipher);
    }

    /**
     * 私钥加密
     *
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return 返回加密子串
     * @throws Exception 加密异常
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        RSAPrivateKey key = (RSAPrivateKey) getPrivateKey(privateKey);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return encrypt(data, key, cipher);
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 公钥
     * @return 解密子串
     * @throws Exception 解密异常
     */
    public static String decryptByPublicKey(String data, String publicKey) throws Exception {
        RSAPublicKey key = (RSAPublicKey) getPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return decrypt(data, key, cipher);
    }

    /**
     * 加密
     *
     * @param data   待加密数据
     * @param key    密钥
     * @param cipher cipher
     * @return 返回加密子串
     * @throws BadPaddingException       异常
     * @throws IllegalBlockSizeException 异常
     */
    private static String encrypt(String data, RSAKey key, Cipher cipher) throws BadPaddingException, IllegalBlockSizeException {
        // 模长
        int keyLen = key.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, keyLen - 11);
        StringBuilder mi = new StringBuilder();
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi.append(bcd2Str(cipher.doFinal(s.getBytes())));
        }
        return mi.toString();
    }

    /**
     * 解密
     *
     * @param data   待解密数据
     * @param key    密钥
     * @param cipher cipher
     * @return 返回解密子串
     * @throws BadPaddingException       异常
     * @throws IllegalBlockSizeException 异常
     */
    private static String decrypt(String data, RSAKey key, Cipher cipher) throws BadPaddingException, IllegalBlockSizeException {
        //模长
        int keyLen = key.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ascii2Bcd(bytes, bytes.length);
        //如果密文长度大于模长则要分组解密
        StringBuilder ming = new StringBuilder();
        byte[][] arrays = splitArray(bcd, keyLen);
        for (byte[] arr : arrays) {
            ming.append(new String(cipher.doFinal(arr)));
        }
        return ming.toString();
    }

    /**
     * ASCII码转BCD码
     */
    private static byte[] ascii2Bcd(byte[] ascii, int ascLen) {
        byte[] bcd = new byte[ascLen / 2];
        int j = 0;
        for (int i = 0; i < (ascLen + 1) / 2; i++) {
            bcd[i] = asc2Bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= ascLen) ? 0x00 : asc2Bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    private static byte asc2Bcd(byte asc) {
        byte bcd;
        if ((asc >= '0') && (asc <= '9')) {
            bcd = (byte) (asc - '0');
        } else if ((asc >= 'A') && (asc <= 'F')) {
            bcd = (byte) (asc - 'A' + 10);
        } else if ((asc >= 'a') && (asc <= 'f')) {
            bcd = (byte) (asc - 'a' + 10);
        } else {
            bcd = (byte) (asc - 48);
        }
        return bcd;
    }

    /**
     * BCD转字符串
     */
    private static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    private static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str;
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    private static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    /**
     * 将PublicKey实例转成base64编码后的公钥字符串
     *
     * @param publicKey 公钥
     * @return 公钥
     */
    public static String getPublicKey2Str(PublicKey publicKey) {
        // 得到公钥字符串
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 将PrivateKey实例转成base64编码后的私钥字符串
     *
     * @param privateKey 私钥
     * @return 私钥字符串
     */
    public static String getPrivateKey2Str(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 将base64编码后的公钥字符串转成PublicKey实例
     *
     * @param publicKey 公钥
     * @return 公钥
     * @throws Exception 异常
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例
     *
     * @param privateKey 私钥
     * @return 私钥
     * @throws Exception 异常
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> keyMap = getKeys();
        String message = "加密信息";
        String result = encryptByPrivateKey(message, keyMap.get(PRIVATE_KEY));
        System.out.println(result);
        System.out.println(decryptByPublicKey(result, keyMap.get(PUBLIC_KEY)));
    }
}
