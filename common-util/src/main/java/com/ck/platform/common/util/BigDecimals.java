package com.ck.platform.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数字计算工具类
 *
 * <p>
 * 问题：new BigDecimal(double/float) 存在精度丢失的问题。
 * 分析：Java中的简单浮点数类型float和double不能够进行运算，或者运算会丢失精度，float和double只能用来做科学计算或者是工程计算，在商业计算中我们要用 java.math.BigDecimal
 * 方案：使用 new BigDecimal(String) 或 BigDecimal.valueOf(double/float)
 *
 * @author chenck
 * @date 2020/3/3 9:55
 */
public class BigDecimals {

    private BigDecimals() {
        throw new AssertionError("No BigDecimals instances for you!");
    }

    /**
     * 默认的精确度 2
     */
    private static final int DEFAULT_SCALE = 2;

    /**
     * 转BigDecimal，并保留2位小数
     */
    public static BigDecimal toBigDecimal(Object obj) {
        String num = "0.00";
        if (obj != null) {
            num = String.valueOf(obj);
        }
        return halfUp2scale(new BigDecimal(num));
    }

    // ===== 加法运算

    /**
     * 加法运算，由scale参数指定精度，roundingMode指定舍入模式。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v1.add(v2).setScale(scale, roundingMode);
    }

    /**
     * 加法运算，由scale参数指定精度，roundingMode指定舍入模式。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2, int scale, RoundingMode roundingMode) {
        return add(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 加法运算（未设置精确度）
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    /**
     * 加法运算（未设置精确度）
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        return add(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2)).doubleValue();
    }

    /**
     * 加法运算，精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add2scale(BigDecimal v1, BigDecimal v2) {
        return add(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 加法运算，精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add2scale(double v1, double v2) {
        return add(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }


    // ===== 减法运算

    /**
     * 减法运算，由scale参数指定精度，roundingMode指定舍入模式。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v1.subtract(v2).setScale(scale, roundingMode);
    }

    /**
     * 减法运算，由scale参数指定精度，roundingMode指定舍入模式。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2, int scale, RoundingMode roundingMode) {
        return sub(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 减法运算（未设置精确度）
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    /**
     * 减法运算（未设置精确度）
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        return sub(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2)).doubleValue();
    }

    /**
     * 减法运算，精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub2scale(BigDecimal v1, BigDecimal v2) {
        return sub(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 减法运算，精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub2scale(double v1, double v2) {
        return sub(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    // ===== 乘法运算

    /**
     * 乘法运算，由scale参数指定精度，roundingMode指定舍入模式。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v1.multiply(v2).setScale(scale, roundingMode);
    }

    /**
     * 乘法运算，由scale参数指定精度，roundingMode指定舍入模式。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2, int scale, RoundingMode roundingMode) {
        return mul(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 乘法运算（未设置精确度）
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    /**
     * 乘法运算（未设置精确度）
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        return mul(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2)).doubleValue();
    }

    /**
     * 乘法运算，精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul2scale(BigDecimal v1, BigDecimal v2) {
        return mul(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 乘法运算，精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul2scale(double v1, double v2) {
        return mul(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    // ===== 除法运算

    /**
     * 除法运算。当发生除不尽的情况时，由scale参数指定精度，roundingMode指定舍入模式。
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度
     * @param roundingMode 舍入模式
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v1.divide(v2, scale, roundingMode);
    }

    /**
     * 除法运算。当发生除不尽的情况时，由scale参数指定精度，roundingMode指定舍入模式。
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度
     * @param roundingMode 舍入模式
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale, RoundingMode roundingMode) {
        return div(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        return div(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2), scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 除法运算，当发生除不尽的情况时，精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div2scale(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 除法运算，当发生除不尽的情况时，精确到小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div2scale(double v1, double v2) {
        return div(v1, v2, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    // ===== 四舍五入

    /**
     * 四舍五入
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal halfUp(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 四舍五入
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double halfUp(double v, int scale) {
        return halfUp(BigDecimal.valueOf(v), scale).doubleValue();
    }

    /**
     * 四舍五入，精确到小数点2位
     *
     * @param v 需要四舍五入的数字
     * @return 四舍五入后的结果
     */
    public static BigDecimal halfUp2scale(BigDecimal v) {
        return halfUp(v, DEFAULT_SCALE);
    }

    /**
     * 四舍五入，精确到小数点2位
     *
     * @param v 需要四舍五入的数字
     * @return 四舍五入后的结果
     */
    public static double halfUp2scale(double v) {
        return halfUp(v, DEFAULT_SCALE);
    }


    public static void main(String[] args) {
        // 精度丢失场景
        BigDecimal b11 = new BigDecimal(1.745);
        BigDecimal b22 = new BigDecimal(0.745);
        System.out.println("精度丢失场景");
        System.out.println(b11);// 1.74500000000000010658141036401502788066864013671875
        System.out.println(b22);// 0.74499999999999999555910790149937383830547332763671875
        System.out.println(b11.setScale(2, BigDecimal.ROUND_HALF_UP));// 结果为 1.75
        System.out.println(b22.setScale(2, BigDecimal.ROUND_HALF_UP));// 结果为 0.74
        System.out.println();

        // 初始数据
        double v1 = 1.745;
        double v2 = 0.745;
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);

        // 加法运算
        System.out.println("加法运算");
        System.out.println("add BigDecimal 5=> " + BigDecimals.add(b1, b2, 5, RoundingMode.HALF_UP));
        System.out.println("add BigDecimal => " + BigDecimals.add(b1, b2));
        System.out.println("add double 5=> " + BigDecimals.add(v1, v2, 5, RoundingMode.HALF_UP));
        System.out.println("add double => " + BigDecimals.add(v1, v2));
        System.out.println("add2scale => " + BigDecimals.add2scale(b1, b2));
        System.out.println("add2scale => " + BigDecimals.add2scale(v1, v2));
        System.out.println();

        // 减法运算
        System.out.println("减法运算");
        System.out.println("sub BigDecimal 5=> " + BigDecimals.sub(b1, b2, 5, RoundingMode.HALF_UP));
        System.out.println("sub BigDecimal => " + BigDecimals.sub(b1, b2));
        System.out.println("sub double 5=> " + BigDecimals.sub(v1, v2, 5, RoundingMode.HALF_UP));
        System.out.println("sub double => " + BigDecimals.sub(v1, v2));
        System.out.println("sub2scale => " + BigDecimals.sub2scale(b1, b2));
        System.out.println("sub2scale => " + BigDecimals.sub2scale(v1, v2));
        System.out.println();

        // 乘法运算
        System.out.println("乘法运算");
        System.out.println("mul BigDecimal 5=> " + BigDecimals.mul(b1, b2, 5, RoundingMode.HALF_UP));
        System.out.println("mul BigDecimal => " + BigDecimals.mul(b1, b2));
        System.out.println("mul double 5=> " + BigDecimals.mul(v1, v2, 5, RoundingMode.HALF_UP));
        System.out.println("mul double => " + BigDecimals.mul(v1, v2));
        System.out.println("mul2scale => " + BigDecimals.mul2scale(b1, b2));
        System.out.println("mul2scale => " + BigDecimals.mul2scale(v1, v2));
        System.out.println();

        // 除法运算
        System.out.println("除法运算");
        System.out.println("div BigDecimal 5=> " + BigDecimals.div(b1, b2, 5, RoundingMode.HALF_UP));
        System.out.println("div BigDecimal => " + BigDecimals.div(b1, b2, 1));
        System.out.println("div double 5=> " + BigDecimals.div(v1, v2, 5, RoundingMode.HALF_UP));
        System.out.println("div double => " + BigDecimals.div(v1, v2, 1));
        System.out.println("div2scale => " + BigDecimals.div2scale(b1, b2));
        System.out.println("div2scale => " + BigDecimals.div2scale(v1, v2));
        System.out.println();
    }
}
