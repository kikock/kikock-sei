package com.kcmp.ck.util;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.security.SecureRandom;

/**
 * Created by kikock
 * 封装各种生成唯一性ID算法的工具类
 * @email kikock@qq.com
 **/
public class IdGenerator {

    private static SecureRandom random = new SecureRandom();

    //同一个类加载器上的不同进程引擎共享一个生成器
    private static TimeBasedGenerator timeBasedGenerator;

    private static void ensureGeneratorInitialized() {
        if (timeBasedGenerator == null) {
            synchronized (IdGenerator.class) {
                if (timeBasedGenerator == null) {
                    timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
                }
            }
        }
    }

    /**
     * 封装fasterxml.uuid,生成有序的UUID.
     * @return 返回UUID，中间有-分割
     */
    public static String uuid() {
        ensureGeneratorInitialized();
        return timeBasedGenerator.generate().toString().toUpperCase();
    }

    /**
     * 封装fasterxml.uuid,生成有序的UUID.
     * @return 返回UUID, 中间无-分割.
     */
    public static String uuid2() {
        ensureGeneratorInitialized();
        return timeBasedGenerator.generate().toString().toUpperCase().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     * 不保证唯一性
     * @return 随机数
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    public static void main(String[] args) {
        for (int i = 1; i < 100; i++) {
            System.out.println(IdGenerator.uuid());
//            System.out.println(IdGenerator.uuid2());
//            System.out.println(IdGenerator.randomLong());
        }
    }
}
