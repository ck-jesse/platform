package com.ck.platform.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author chenck
 * @date 2019/11/15 15:49
 */
public class CardNoUtil {

    // 卡号校验位计算的权重
    public static final int[] MEM_CARD_WEIGHT = {8, 27, 19, 26, 9, 23, 15, 24, 31, 4, 16, 30, 14,
            29, 1, 10, 6, 12, 11, 21, 28, 18, 3, 0, 25, 17, 2, 5, 7, 22, 20, 13};

    // 一次性token码校验位计算的权重
    public static final int[] OTT_WEIGHT = {21, 23, 18, 26, 16, 8, 4, 14, 13, 5, 2, 17, 15, 19, 7,
            28, 3, 24, 0, 22, 20, 11, 9, 31, 25, 27, 30, 1, 10, 6, 12, 29};

    public static final int[] CARD_NUMBER_WEIGHT = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};


    /**
     * 生成指定长度numLen的随机数字串，最后一位是校验位
     *
     * @param prefix         数字串前缀
     * @param numLen         数字长度，不包含
     * @param checkSumWeight 生成校验位的权重
     * @return 生成的随机字串，包含前缀和校验位
     */
    public static String genSerialNumWithCheckSum(String prefix, int numLen, int[] checkSumWeight) {
        // 生成的数字串长度需要减去1位校验位
        numLen--;
        if (StringUtils.isNotBlank(prefix)) {
            numLen = numLen - prefix.length();
        }
        // 生成随机数字串
        String code = RandomUtil.genRandomNumber(numLen);

        // 拼接前缀，计算校验和时将除校验位之外的所有的数字都参与计算
        if (StringUtils.isNotBlank(prefix)) {
            code = prefix + code;
        }
        // 计算校验位
        char checkSum = calcCheckSum(code, checkSumWeight);

        return code + checkSum;
    }

    /**
     * 计算校验位 计算规则如下： sum=(a[n-1]xW[n-1])⊕(n-1)+...+(a[0]xW[0])⊕0 checksum = sum % 10;
     *
     * @param code   待计算校验位的字符串
     * @param weight 权重
     * @return 校验位
     */
    public static char calcCheckSum(String code, int[] weight) {
        // 要求权重数组长度不能小于卡号的长度
        if (weight == null || weight.length < code.length()) {
            throw new IllegalArgumentException("check sum weigth length invalid");
        }

        long sum = 0;
        int val = 0;
        for (int i = 0; i < code.length(); ++i) {
            val = code.charAt(i);
            sum = sum + (val * weight[i]) ^ i;
        }

        // 和为负数时将负数转为正数
        if (sum < 0) {
            sum = sum * -1;
        }

        // 将和模10转为数值对应的字母
        char checkCh = (char) ('0' + sum % 10);

        return checkCh;
    }

    /**
     * 验证 code 的有效性
     * 1.prefix不为空，则校验strNumber是否以prefix开头
     * 2.校验code的校验位是否正确
     *
     * @param code           待验证的数字串
     * @param prefix         数字串前缀
     * @param checkSumWeight 生成校验位的权重
     * @return true 表示code校验通过，false 表示code校验失败
     */
    public static boolean validCode(String code, String prefix, int[] checkSumWeight) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        if (StringUtils.isNotBlank(prefix) && !code.startsWith(prefix)) {
            return false;
        }
        String num = code.substring(0, code.length() - 1);
        String checkSum = code.substring(code.length() - 1);
        // 计算校验位
        char checkSumNew = calcCheckSum(num, checkSumWeight);
        if (checkSum.equals(String.valueOf(checkSumNew))) {
            return true;
        }
        return false;
    }

    /**
     * 生成会员卡卡号
     *
     * @param cardBin 卡号前缀
     * @param length  会员卡卡号长度，包含前缀和校验位的长度（即用户所看到的长度）
     */
    public static String genMemCardNum(String cardBin, int length) {
        return genSerialNumWithCheckSum(cardBin.substring(0, 5) + "1" + cardBin.substring(5), length,
                MEM_CARD_WEIGHT);
    }

    /**
     * 生成OTT编码 - 一次性令牌
     * 注：可用于做付款码的有效期控制
     *
     * @param prefix OTT前缀
     * @param length OTT编码长度，包含前缀和校验位的长度（即用户所看到的长度）
     * @return
     */
    public static String genOttCode(String prefix, int length) {
        return genSerialNumWithCheckSum(prefix, length, OTT_WEIGHT);
    }

    /**
     * 验证OTT编码的有效性
     *
     * @param ottCode 待验证的数字串
     * @param prefix  数字串前缀
     * @return
     */
    public static boolean validOttCode(String ottCode, String prefix) {
        return validCode(ottCode, prefix, CardNoUtil.OTT_WEIGHT);
    }

    /**
     * 生成优惠券序列号
     *
     * @return
     */
    public static String genCouponNum(String prefix) {
        int cardNumLen = 16;//定义为16位的随机数字
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String date = sdf.format(new Date());
        StringBuilder sb = new StringBuilder();
        int genNumLen = 0;
        if (StringUtils.isNotBlank(prefix)) {
            prefix = prefix.trim();
            genNumLen = cardNumLen - prefix.length();
        }
        genNumLen = genNumLen - date.length();
        if (genNumLen <= 0) {
            return prefix + date;
        }
        sb.append(prefix + date);
        Random random = new Random();
        for (int i = 0; i < genNumLen - 1; i++) {//保留因为校验
            int index = random.nextInt(10);
            sb.append(CARD_NUMBER_WEIGHT[index]);
        }
        return genSerialNumWithCheckSum(sb.toString(), cardNumLen, MEM_CARD_WEIGHT);
    }

    /**
     * 生成优惠券序列号
     *
     * @param prefix 前缀
     */
    public static String genCouponSn(String prefix) {
        return genSerialNumWithCheckSum(prefix.substring(0, 5) + "1" + prefix.substring(5), 16, MEM_CARD_WEIGHT);
    }

    /**
     * 是否可以加一位机器标识，在分布式环境中做到唯一
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 300000; i++) {
            CardNoUtil.genCouponSn("666666");
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + " ms");
    }

}
