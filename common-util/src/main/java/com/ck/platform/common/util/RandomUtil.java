package com.ck.platform.common.util;

import com.ck.platform.common.util.security.HexCode;

import java.util.Random;

/**
 * 随机数工具类
 *
 * @author chenck
 * @date 2019/3/4 15:58
 */
public class RandomUtil {

    private static final int random_max_len = 32;

    /**
     * 获取指定范围内的随机数
     *
     * @param min
     * @param max
     */
    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 基于UUID获取随机数
     */
    public static String getUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成指定长度的随机数字串
     *
     * @param numLen 随机数字串长度，最大32
     * @return
     */
    public static String genRandomNumber(int numLen) {
        if (numLen < 0 || numLen > random_max_len) {
            throw new IllegalArgumentException("card num length illegal");
        }
        // uuid生成一串随机数（16字节）
        String uuid = RandomUtil.getUUID();
        /**
         * 将随机数的16进制串首尾组合，将对应的10进制模10得到的数字作为卡号的一个数字 a31a30...a0
         * a31a0%10=b15,a16a15%10=b0 b15b14...b0 生成的数字长度为多少位则截取前多少位
         */
        StringBuilder sb = new StringBuilder(random_max_len);
        for (int i = 0; i < numLen; ++i) {
            char chLow = uuid.charAt(i);
            char chHight = uuid.charAt(uuid.length() - i - 1);
            int val = HexCode.decodeByte(chHight, chLow);
            // 如果为负数则转为正数，然的模10
            val = val > 0 ? val % 10 : (val * -1) % 10;
            // 将数字转为字符
            sb.append(String.valueOf(val));
        }
        return sb.substring(0, numLen);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String code = RandomUtil.genRandomNumber(20);
//            System.out.println(code);
//            RandomUtil.getUUID();
//            java.util.UUID.randomUUID();
//            System.out.println(RandomUtil.getUUID());
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + " ms");
    }
}
