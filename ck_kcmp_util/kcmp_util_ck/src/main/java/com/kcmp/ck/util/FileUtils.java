package com.kcmp.ck.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by kikock
 * 实现文件或InputStream与base64编码字符串的相互转化
 * 使用apache common包中的org.apache.commons.codec.binary.Base64类进行编码和解码
 * @email kikock@qq.com
 **/
public final class FileUtils {

    /**
     * 文件转为Base64字符串
     * @param file 文件
     * @return base64编码字符串
     * @throws IOException
     */
    public static String file2Str(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }

        String str;
        byte[] b = Files.readAllBytes(Paths.get(file.getPath()));
        str = Base64.encodeBase64String(b);
        return str;
    }

    /**
     * 输入流转为Base64字符串
     * @param inputStream 输入流
     * @return base64编码字符串
     * @throws IOException
     */
    public static String stream2Str(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        String str;
        byte[] data;
        // 读取图片字节数组
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[inputStream.available()];
            int rc;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }
        // 返回Base64编码过的字节数组字符串
        str = Base64.encodeBase64String(data);

        return str;
    }

    /**
     * @param base64Str Base64编码字符串
     * @param filePath  文件路径
     * @return 返回文件
     * @throws Exception
     */
    public static File str2File(String base64Str, String filePath) throws Exception {
        if (base64Str == null || base64Str.trim().length() == 0) {
            return null;
        }

        byte[] decode = decodeBase64(base64Str);

        Files.write(Paths.get(filePath), decode, StandardOpenOption.CREATE);
        File file = new File(filePath);
        return file;
    }

    /**
     * @param base64Str Base64编码字符串
     * @return 返回输入流
     * @throws IOException
     */
    public static InputStream str2InputStream(String base64Str) throws IOException {
        if (base64Str == null || base64Str.trim().length() == 0) {
            return null;
        }

        ByteArrayInputStream stream;
        byte[] decode = decodeBase64(base64Str);
        stream = new ByteArrayInputStream(decode);
        return stream;
    }

    public static byte[] decodeBase64(String base64Str) {
        if (base64Str == null || base64Str.trim().length() == 0) {
            throw new IllegalArgumentException("not null.");
        }
        byte[] decode;
        if (base64Str.contains("image")) {
            if (base64Str.contains("data:image/bmp;base64,")) {
                decode = Base64.decodeBase64(base64Str.replaceAll("data:image/bmp;base64,", ""));
            } else {
                if (base64Str.contains("data:image/jpeg;base64,")) {
                    decode = Base64.decodeBase64(base64Str.replaceAll("data:image/jpeg;base64,", ""));
                } else {
                    if (base64Str.contains("data:image/png;base64,")) {
                        decode = Base64.decodeBase64(base64Str.replaceAll("data:image/png;base64,", ""));
                    } else {
                        decode = Base64.decodeBase64(base64Str.replaceAll("data:image/jpg;base64,", ""));
                    }
                }
            }
        } else {
            decode = Base64.decodeBase64(base64Str);
        }
        return decode;
    }

    public static void main(String[] args) {
        File file = new File("E:\\download\\201912231425350876.doc");
        String s = null;
        try {
            s = FileUtils.file2Str(file);

            InputStream stream = FileUtils.str2InputStream(s);
            File file1 = new File("E:\\download\\123.doc");
            OutputStream os = new FileOutputStream(file1);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            stream.close();

            InputStream in = new FileInputStream(file1);

            String s1 = FileUtils.stream2Str(in);
            System.out.println(s1);
            System.out.println(s.equals(s1));

            File file2 = FileUtils.str2File(s1, "E:\\download\\12.doc");
            System.out.println(file2.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }
}
