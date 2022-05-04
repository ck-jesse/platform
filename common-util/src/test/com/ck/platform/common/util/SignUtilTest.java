package com.ck.platform.common.util;

import com.ck.platform.common.util.security.Base64Util;
import com.ck.platform.common.util.security.RSA;
import com.ck.platform.common.util.security.SignUtil;
import com.ck.platform.common.util.security.Tuple;
import org.junit.Test;

/**
 * @author chenck
 * @date 2022/5/4 15:22
 */
public class SignUtilTest {

    public static final String signData = "1234567890asdfaqwrjkluohjjkluiojkluiojkhjlklkjkjklkkjdjdfkjlflkj";
    public static final String charset = "utf-8";

    @Test
    public void testRSA() {
        String sign_type = "rsa-256";
        System.out.println("签名类型=" + sign_type);

        // 生成RSA密钥对
        Tuple.Tuple2<String, String> keys = RSA.genKeyPairStr("1234", 1024);
        String publicKey = keys._1();
        String privateKey = keys._2();
        System.out.println("公钥");
        System.out.println(Base64Util.formatBase64Str(publicKey));
        System.out.println("私钥");
        System.out.println(Base64Util.formatBase64Str(privateKey));

        // 生成签名
        String sign = SignUtil.genSign(signData, charset, sign_type, privateKey);
        System.out.println("签名=" + sign);

        // 校验签名
        boolean check = SignUtil.checkSign(signData, charset, sign_type, sign, publicKey);
        System.out.println("验签=" + check);
    }


    @Test
    public void testMD5() {
        String sign_type = "md5";
        System.out.println("签名类型=" + sign_type);

        String key = "abcdefg";

        // 生成签名
        String sign = SignUtil.genSign(signData, charset, sign_type, key);
        System.out.println("签名=" + sign);

        // 校验签名
        boolean check = SignUtil.checkSign(signData, charset, sign_type, sign, key);
        System.out.println("验签=" + check);
    }

    @Test
    public void testSHA1() {
        String sign_type = "sha1";
        System.out.println("签名类型=" + sign_type);

        String key = "abcdefg";

        // 生成签名
        String sign = SignUtil.genSign(signData, charset, sign_type, key);
        System.out.println("签名=" + sign);

        // 校验签名
        boolean check = SignUtil.checkSign(signData, charset, sign_type, sign, key);
        System.out.println("验签=" + check);
    }

    @Test
    public void testSHA256() {
        String sign_type = "sha-256";
        System.out.println("签名类型=" + sign_type);

        String key = "abcdefg";

        // 生成签名
        String sign = SignUtil.genSign(signData, charset, sign_type, key);
        System.out.println("签名=" + sign);

        // 校验签名
        boolean check = SignUtil.checkSign(signData, charset, sign_type, sign, key);
        System.out.println("验签=" + check);
    }
}
