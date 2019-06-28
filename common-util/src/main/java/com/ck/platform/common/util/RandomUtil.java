package com.ck.platform.common.util;

import java.util.Random;

/**
 * @author chenck
 * @date 2019/3/4 15:58
 */
public class RandomUtil {

    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static String getUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

}
