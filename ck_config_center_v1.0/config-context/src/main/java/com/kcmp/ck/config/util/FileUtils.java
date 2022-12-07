package com.kcmp.ck.config.util;

import com.kcmp.ck.config.exception.ServiceException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 实现功能：
 * 实现文件或InputStream与base64编码字符串的相互转化
 * 针对比较长的原文进行base64编码可以得到如下结论：
 * jdk7的编码结果包含换行；
 * jdk8的编码结果不包含换行；
 * jdk8无法解码包含换行的编码结果;
 * 因此,使用apache common包中的org.apache.commons.codec.binary.Base64类进行编码和解码；
 * rfc1521、rfc2045和rfc4648关于base64的部分不一样
 * https://blog.csdn.net/java_4_ever/article/details/80978089
 *
 * @author kikock
 * @version 1.0.0
 */
public final class FileUtils extends org.apache.commons.io.FileUtils {
    public static final String DOT = ".";
    public static final String SLASH_ONE = "/";
    public static final String SLASH_TWO = "\\";
    public static final String PDF = "pdf";
    public static final String HTML = "html";

    /**
     * 获取含扩展名的文件名（不包含path路径）
     */
    public static String getFileName(String fileName) {
        String name;
        if (StringUtils.lastIndexOf(fileName, SLASH_ONE) >= StringUtils.lastIndexOf(fileName, SLASH_TWO)) {
            name = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, SLASH_ONE) + 1,
                    fileName.length());
        } else {
            name = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, SLASH_TWO) + 1,
                    fileName.length());
        }
        return StringUtils.trimToEmpty(name);
    }

    /**
     * 获取没有扩展名的文件名
     */
    public static String getWithoutExtension(String fileName) {
        String ext = StringUtils.substring(fileName, 0,
                StringUtils.lastIndexOf(fileName, DOT) == -1 ? fileName.length() : StringUtils.lastIndexOf(fileName, DOT));
        return StringUtils.trimToEmpty(ext);
    }

    /**
     * 获取扩展名
     */
    public static String getExtension(String fileName) {
        if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, DOT)) {
            return StringUtils.EMPTY;
        }
        String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, DOT) + 1);
        return StringUtils.trimToEmpty(ext);
    }

    /**
     * 判断是否同为扩展名
     */
    public static boolean isExtension(String fileName, String ext) {
        return StringUtils.equalsIgnoreCase(getExtension(fileName), ext);
    }

    /**
     * 判断是否存在扩展名
     */
    public static boolean hasExtension(String fileName) {
        return !isExtension(fileName, StringUtils.EMPTY);
    }

    /**
     * 得到正确的扩展名
     */
    public static String trimExtension(String ext) {
        return getExtension(DOT + ext);
    }

    /**
     * 向path中填充扩展名(如果没有或不同的话)
     */
    public static String fillExtension(String fileName, String ext) {
        fileName = replacePath(fileName + DOT);
        ext = trimExtension(ext);
        if (!hasExtension(fileName)) {
            return fileName + getExtension(ext);
        }
        if (!isExtension(fileName, ext)) {
            return getWithoutExtension(fileName) + getExtension(ext);
        }
        return fileName;
    }

    /**
     * 判断是否是文件PATH
     */
    public static boolean isFile(String fileName) {
        return hasExtension(fileName);
    }

    /**
     * 判断是否是文件夹PATH
     */
    public static boolean isFolder(String fileName) {
        return !hasExtension(fileName);
    }

    public static String replacePath(String path) {
        return StringUtils.replace(StringUtils.trimToEmpty(path), SLASH_ONE, SLASH_TWO);
    }

    /**
     * 链接PATH前处理
     */
    public static String trimLeftPath(String path) {
        if (isFile(path)) {
            return path;
        }
        path = replacePath(path);
        String top = StringUtils.left(path, 1);
        if (StringUtils.equalsIgnoreCase(SLASH_TWO, top)) {
            return StringUtils.substring(path, 1);
        }
        return path;
    }

    /**
     * 链接PATH后处理
     */
    public static String trimRightPath(String path) {
        if (isFile(path)) {
            return path;
        }
        path = replacePath(path);
        String bottom = StringUtils.right(path, 1);
        if (StringUtils.equalsIgnoreCase(SLASH_TWO, bottom)) {
            return StringUtils.substring(path, 0, path.length() - 2);
        }
        return path + SLASH_TWO;
    }

    /**
     * 链接PATH前后处理，得到准确的链接PATH
     */
    public static String trimPath(String path) {
        path = StringUtils.replace(StringUtils.trimToEmpty(path), SLASH_ONE, SLASH_TWO);
        path = trimLeftPath(path);
        path = trimRightPath(path);
        return path;
    }

    /**
     * 通过数组完整链接PATH
     */
    public static String bulidFullPath(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            sb.append(trimPath(path));
        }
        return sb.toString();
    }

    public static void delFile(String filepath) {
        File f = new File(filepath);//定义文件路径
        if (f.exists() && f.isDirectory()) {//判断是文件还是目录
            if (f.listFiles().length != 0) {
                //若有则把文件放进数组，并判断是否有下级目录
                File[] delFile = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        delFile(delFile[j].getAbsolutePath());//递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();//删除文件
                }
            }
            f.delete();
        }
    }

    public static void delFileList(List<String> filePaths) {
        for (String filePath : filePaths) {
            delFile(filePath);
        }
    }

    /**
     * 获取路径下的所有文件/文件夹
     *
     * @param directoryPath  需要遍历的文件夹路径
     * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
     */
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<String>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (isAddDirectory) {
                    list.add(file.getAbsolutePath());
                }
                list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
            } else {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    /**
     * 文件转为Base64字符串
     *
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
     *
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

    public static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 拆分byte数组
     *
     * @param bytes 要拆分的数组
     * @param size  要按几个组成一份
     * @return
     */
    public static byte[][] splitBytes(byte[] bytes, int size) {
        double splitLength = Double.parseDouble(size + "");
        int arrayLength = (int) Math.ceil(bytes.length / splitLength);
        byte[][] result = new byte[arrayLength][];
        int from, to;
        for (int i = 0; i < arrayLength; i++) {
            from = (int) (i * splitLength);
            to = (int) (from + splitLength);
            if (to > bytes.length) {
                to = bytes.length;
            }
            result[i] = Arrays.copyOfRange(bytes, from, to);
        }
        return result;
    }

    /**
     * @param data      数据
     * @param totalSize 数据流的大小
     * @param chunkSize 每次读取数据流的大小
     */
    public static void splitChunks(final byte[] data, final long totalSize, final int chunkSize, byte[][] chunkData, Set<Integer> excludeChunks) {
        //已经读取的数据的大小
        int readSize = 0;
        int size = totalSize > chunkSize ? chunkSize : data.length;
        // 分块序号
        int index = 0;
        int len;
        byte[] buffer = new byte[size];
        try (InputStream stream = new ByteArrayInputStream(data)) {
            while ((len = stream.read(buffer, 0, size)) > 0) {
                readSize += len;
                if (excludeChunks.contains(index)) {
                    index++;
                    buffer = new byte[size];
                    continue;
                }
                chunkData[index++] = buffer;

                //如果数据流的总长度减去已经读取的数据流的长度值小于每次读取数据流的设定的大小，那么就重新为buffer字节数组设定大小
                if ((totalSize - readSize) < size) {
                    //这样可以避免最终得到的数据的结尾处多出多余的空值
                    size = (int) totalSize - readSize;
                    buffer = new byte[size];
                } else {
                    buffer = new byte[size];
                }
            }
        } catch (IOException e) {
            throw new ServiceException("数据流分块异常", e);
        }
    }

    public static void main(String[] args) {
        File file = new File("/Users/chaoma/Downloads/电子档案单附件批量下载及附件水印功能需求规格说明.doc");
        String s = null;
        try {
            s = FileUtils.file2Str(file);

            InputStream stream = FileUtils.str2InputStream(s);
            File file1 = new File("/Users/chaoma/Downloads/123.doc");
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

            File file2 = FileUtils.str2File(s1, "/Users/chaoma/Downloads/12.doc");
            System.out.println(file2.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }
}
