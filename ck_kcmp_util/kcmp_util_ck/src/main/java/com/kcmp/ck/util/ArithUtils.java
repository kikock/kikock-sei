package com.kcmp.ck.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by kikock
 * 算术工具类.
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入
 * @email kikock@qq.com
 **/
public class ArithUtils {
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 这个类不能实例化
     */
    private ArithUtils() {
    }

    /**
     * 提供精确加法计算的add方法
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        return round(b1.add(b2).doubleValue(), DEF_DIV_SCALE);
    }

    /**
     * 提供精确加法计算的add方法
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal value1, double value2) {
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        return value1.add(b2);
    }

    /**
     * 提供精确减法运算的sub方法
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        return round(b1.subtract(b2).doubleValue(), DEF_DIV_SCALE);
    }

    /**
     * 提供精确减法运算的sub方法
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal value1, double value2) {
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        return value1.subtract(b2);
    }

    /**
     * 提供精确乘法运算的mul方法
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        return round(b1.multiply(b2).doubleValue(), DEF_DIV_SCALE);
    }

    /**
     * 提供精确乘法运算的mul方法
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal value1, double value2) {
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        return value1.multiply(b2);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供精确的除法运算方法div
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     */
    public static double div(double value1, double value2, int scale) {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalArgumentException("除法运算刻度必须是正整数或零");
        }
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("四舍五入刻度必须是正整数或零");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String format(double v, int scale) {
        StringBuilder bf = new StringBuilder("0.");
        for (int i = 0; i < scale; i++) {
            bf.append("0");
        }
        DecimalFormat format = new DecimalFormat(bf.toString());
        return format.format(round(v, scale));
    }

    public static void main(String[] args) {
        Double a=485968.32;
        Double b=38485.113;
        Double c =  ArithUtils.add(a, b);
        System.out.println(ArithUtils.round(c, 1));
    }
}
