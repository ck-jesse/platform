package com.ck.platform.common.util;

import java.util.Random;

/**
 * 随机生成字母或数字的组合
 *
 * @author chenck
 * @date 2022/2/13 22:25
 */
public class RandomGenStrUtil {

    private static final int Num_WORD = 1;//数字
    private static final int STR_WORD = 2;//字母
    private static final int STR_NUM_WORD = 3;//字母数字
    private static final int MIX_WORD = 4;//字母数字符号

    /**
     * 获取随机字符串
     *
     * @param style  输出格式  1纯数字，2纯字符串，3字符数字组合，4字符数字符号组合。
     * @param length 输出长度
     */
    public static String getRandomStr(int style, int length) {
        if (style == Num_WORD) {
            return getNumRandom(length);
        } else if (style == STR_WORD) {
            return getStrRandom(length);
        } else if (style == STR_NUM_WORD) {
            return getStrNumRandom(length);
        } else if (style == MIX_WORD) {
            return getMixRandom(length);
        } else {
            return getMixRandom(length);
        }
    }

    //纯数字
    private static String getNumRandom(int length) {
        int[] array = new int[length];
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * 10);
            str.append(array[i]);
        }
        return str.toString();
    }

    //纯字母
    private static String getStrRandom(int length) {
        int[] array = new int[length];
        char[] chars = new char[length];
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            while (true) {
                array[i] = (int) (Math.random() * 1000);
                if ((array[i] > 64 && array[i] < 91)
                        || (array[i] > 96 && array[i] < 123))
                    break;
            }
            chars[i] = (char) array[i];
            str.append(chars[i]);
        }
        return str.toString();
    }

    //字母数字组合
    public static String getStrNumRandom(Integer length) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            boolean b = random.nextBoolean();
            if (b) { // 字符串
                int choice = random.nextBoolean() ? 65 : 97; //取得65大写字母还是97小写字母
                str.append((char) (choice + random.nextInt(26)));// 取得大写字母
            } else { // 数字
                str.append(random.nextInt(10));
            }
        }
        return str.toString();
    }

    //字母数字符号组合
    private static String getMixRandom(int length) {
        int[] array = new int[length];
        char[] chars = new char[length];
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < length; i++) {
            while (true) {
                array[i] = (int) (Math.random() * 1000);
                if (array[i] > 47 && array[i] < 91 || (array[i] > 96 && array[i] < 123))
                    break;
            }
            chars[i] = (char) array[i];
            str.append(chars[i]);
        }
        return str.toString();
    }

    public static void main(String[] args) {
        System.out.println(RandomGenStrUtil.getRandomStr(2, 10));
        System.out.println(RandomGenStrUtil.getNumRandom(10));
        System.out.println(RandomGenStrUtil.getStrRandom(10));
        System.out.println(RandomGenStrUtil.getStrNumRandom(10));
        System.out.println(RandomGenStrUtil.getMixRandom(10));
    }
}
