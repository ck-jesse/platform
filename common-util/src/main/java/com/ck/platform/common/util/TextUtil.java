package com.ck.platform.common.util;

/**
 * 字符串工具类
 *
 * @Author chenck
 * @Date 2018/3/22 11:01
 */
public class TextUtil {

    /**
     * 字符串为null或长度为0时，返回true
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 14:36
     */
    public static boolean isEmpty(final CharSequence s) {
        if (s == null) {
            return true;
        }
        return s.length() == 0;
    }

    /**
     * 字符串为null或空白符（空格、tab键、换行符）时，返回true
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 14:28
     */
    public static boolean isBlank(final CharSequence s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串含有空白符（空格、tab键、换行符）时，返回true
     *
     * @Param
     * @Author chenck
     * @Date 2018/3/22 14:38
     */
    public static boolean containsBlanks(final CharSequence s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
