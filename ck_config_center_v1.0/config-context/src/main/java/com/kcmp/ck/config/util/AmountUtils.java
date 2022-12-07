package com.kcmp.ck.config.util;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * 实现功能：分元转换
 *
 * @author kikock
 * @version 1.0.0
 */
public final class AmountUtils {
    /**
     * 缺省的币种代码，为CNY（人民币）。
     */
    public static final String DEFAULT_CURRENCY_CODE = "CNY";

    public static Double changeF2Y(Double cent) {
        if (null == cent) {
            return 0D;
        }
        long centLongVal = cent.longValue(); // 强制转换
        Currency currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
        BigDecimal amount = BigDecimal.valueOf(centLongVal, currency.getDefaultFractionDigits());
        return amount.doubleValue();
    }

    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     */
    public static Double changeY2F(Double amountD) {
        if (amountD == null) {
            return 0d;
        }
        BigDecimal amount = new BigDecimal(amountD.toString());
        long cent = rounding(amount.movePointRight(2), BigDecimal.ROUND_HALF_EVEN);
        return (double) cent;
    }

    /**
     * 对BigDecimal型的值按指定取整方式取整。
     *
     * @param val          待取整的BigDecimal值
     * @param roundingMode 取整方式
     * @return 取整后的long型值
     */
    public static long rounding(BigDecimal val, int roundingMode) {
        return val.setScale(0, roundingMode).longValue();
    }

    public static void main(String[] args) {
        try {
            System.out.println(AmountUtils.changeY2F(2999.00));
            System.out.println(AmountUtils.changeF2Y(222.00));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
