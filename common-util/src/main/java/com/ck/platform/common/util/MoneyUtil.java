package com.ck.platform.common.util;

import java.math.BigDecimal;

/**
 * 钱处理工具类
 *
 * @author chenck
 * @date 2019/6/28 16:41
 */
public class MoneyUtil {

    public static final BigDecimal ZERO = new BigDecimal("0.00");

    /**
     * 判断是否等于0.00,null也表示等于0.00
     *
     * @param amt
     * @return
     */
    public static boolean isZero(BigDecimal amt) {
        if (null == amt)
            return true;

        if (0 == amt.compareTo(ZERO))
            return true;

        return false;
    }

    /**
     * 判断是否大于0
     *
     * @param amt
     * @return
     */
    public static boolean isPositive(BigDecimal amt) {
        if (null == amt)
            return false;

        if (0 < amt.compareTo(ZERO))
            return true;
        else
            return false;
    }

    /**
     * 金额是否小于0，null表示金额为0返回false
     *
     * @param amt
     * @return true-小于0
     */
    public static boolean isNegative(BigDecimal amt) {
        if (null == amt)
            return false;

        if (0 > amt.compareTo(ZERO))
            return true;
        else
            return false;
    }

    /**
     * 分转元
     *
     * @param fen          金额(分)
     * @param scale        小数点
     * @param roundingMode 舍入模式
     * @return
     */
    public static BigDecimal fen2yuan(BigDecimal fen, int scale, int roundingMode) {
        return (null == fen ? BigDecimal.valueOf(0).setScale(scale)
                : fen.divide(BigDecimal.valueOf(100), scale, roundingMode)).setScale(scale);
    }

    /**
     * 分转元(2位小数)，四舍五入
     *
     * @param fen 金额(分)
     * @return
     */
    public static BigDecimal fen2yuan(BigDecimal fen) {
        return fen2yuan(fen, 2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 分转元(6位小数)，四舍五入
     *
     * @param fen 金额(分)
     * @return
     */
    public static BigDecimal fen2yuan6scale(BigDecimal fen) {
        return fen2yuan(fen, 6, BigDecimal.ROUND_DOWN);
    }

    /**
     * 元转分
     *
     * @param yuan  金额(元)
     * @param scale 小数点
     * @return
     */
    public static BigDecimal yuan2fen(BigDecimal yuan, int scale) {
        return (null == yuan ? BigDecimal.valueOf(0).setScale(scale)
                : yuan.multiply(BigDecimal.valueOf(100)).setScale(scale));
    }

    /**
     * 元转分(2位小数)
     *
     * @param yuan 金额(元)
     * @return
     */
    public static BigDecimal yuan2fen(BigDecimal yuan) {
        return yuan2fen(yuan, 2);
    }

}
