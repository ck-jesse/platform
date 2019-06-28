package com.ck.platform.common.util;

import java.util.Collection;

/**
 * 参数验证
 *
 * @Author chenck
 * @Date 2018/3/22 11:01
 */
public class ArgumentUtil {

    /**
     * 布尔表达式不能为false
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 14:16
     */
    public static void check(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 布尔表达式不能为false，且多参数格式化处理消息
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 14:16
     */
    public static void check(final boolean expression, final String message, final Object... args) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    /**
     * 布尔表达式不能为false，且单参数格式化处理消息
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 14:16
     */
    public static void check(final boolean expression, final String message, final Object arg) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, arg));
        }
    }

    /**
     * 对象不能为null
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 14:15
     */
    public static <T> T notNull(final T argument, final String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " 不能为null");
        }
        return argument;
    }

    /**
     * 字符串不能为null或字符串长度不能等于0
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 13:45
     */
    public static <T extends CharSequence> T notEmpty(final T argument, final String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " 不能为null");
        }
        if (TextUtil.isEmpty(argument)) {
            throw new IllegalArgumentException(name + " 不能为空");
        }
        return argument;
    }

    /**
     * 字符串不能为null或不能仅仅只含有空白字符
     * 注：空白符包含：空格、tab键、换行符
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 13:45
     */
    public static <T extends CharSequence> T notBlank(final T argument, final String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " 不能为null");
        }
        if (TextUtil.isBlank(argument)) {
            throw new IllegalArgumentException(name + " 不能为空白字符");
        }
        return argument;
    }

    /**
     * 字符串不能为null或不能包含空白字符
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 13:49
     */
    public static <T extends CharSequence> T containsNoBlanks(final T argument, final String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " 不能为null");
        }
        if (TextUtil.containsBlanks(argument)) {
            throw new IllegalArgumentException(name + " 不能包含空白字符");
        }
        return argument;
    }

    /**
     * 集合不能为null或空
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 12:04
     */
    public static <E, T extends Collection<E>> T notEmpty(final T argument, final String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " 不能为null");
        }
        if (argument.isEmpty()) {
            throw new IllegalArgumentException(name + " 不能为空");
        }
        return argument;
    }

    /**
     * 不能为负数或零
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 11:56
     */
    public static int positive(final int n, final String name) {
        if (n <= 0) {
            throw new IllegalArgumentException(name + " 不能为负数或零");
        }
        return n;
    }

    /**
     * 不能为负数或零
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 11:56
     */
    public static long positive(final long n, final String name) {
        if (n <= 0) {
            throw new IllegalArgumentException(name + " 不能为负数或零");
        }
        return n;
    }

    /**
     * 不能为负数
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 11:56
     */
    public static int notNegative(final int n, final String name) {
        if (n < 0) {
            throw new IllegalArgumentException(name + " 不能为负数");
        }
        return n;
    }

    /**
     * 不能为负数
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 11:56
     */
    public static long notNegative(final long n, final String name) {
        if (n < 0) {
            throw new IllegalArgumentException(name + " 不能为负数");
        }
        return n;
    }
}
