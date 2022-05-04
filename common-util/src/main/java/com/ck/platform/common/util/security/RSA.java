package com.ck.platform.common.util.security;

import com.ck.platform.common.util.security.Tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    private static final Logger logger = LoggerFactory.getLogger(RSA.class);

    /**
     * 加密算法 RSA
     */
    protected static String ALGORITHM_RSA = "RSA";

    /**
     * 签名验签算法 SHA1
     */
    protected static String SIGN_ALGORITHM_SHA1 = "SHA1WithRSA";

    /**
     * 签名验签算法 SHA256
     */
    protected static String SIGN_ALGORITHM_SHA256 = "SHA256WithRSA";

    /**
     * RSA最大加密明文大小
     */
    protected static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    protected static final int MAX_DECRYPT_BLOCK = 128;

    // ============= 1. 生成RSA公私钥

    /**
     * 生成RSA公私钥
     *
     * @param keyRandom 随机串
     * @param keySize   密钥长度
     * @return
     */
    public static Tuple2<PublicKey, PrivateKey> genKeyPair(String keyRandom, int keySize) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM_RSA);

            generator.initialize(keySize, new SecureRandom(keyRandom.getBytes()));
            KeyPair keyPair = generator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            return new Tuple2<PublicKey, PrivateKey>(publicKey, privateKey);
        } catch (NoSuchAlgorithmException e) {
            logger.error("not support RSA", e);
        } catch (Throwable t) {
            logger.error("generate RSA key fail", t);
        }
        return null;
    }

    /**
     * 生成RSA公私钥，以字串形返回公私钥
     *
     * @param keyRandom
     * @param keySize
     * @return 返回的_1()是公钥，_2()是私钥
     */
    public static Tuple2<String, String> genKeyPairStr(String keyRandom, int keySize) {
        Tuple2<PublicKey, PrivateKey> keyPair = genKeyPair(keyRandom, keySize);
        if (keyPair == null) {
            return null;
        }

        // base64 编码
        String publicKeyStr = Base64Util.encodeBase64String(keyPair._1().getEncoded());
        String privateKeyStr = Base64Util.encodeBase64String(keyPair._2().getEncoded());

        return new Tuple2<String, String>(publicKeyStr, privateKeyStr);
    }

    // ============= 2. 提取密钥对象

    /**
     * 根据公钥串生成公钥KEY对象
     *
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKeyStr) {
        try {
            // base64 解码
            byte[] keyBytes = Base64Util.decodeBase64(publicKeyStr);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            logger.error("RSA getPublicKey fail", e);
        }
        return null;
    }

    /**
     * 根据私钥串生成私钥KEY对象
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKeyStr) {
        try {
            // base64 解码
            byte[] keyBytes = Base64Util.decodeBase64(privateKeyStr);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            logger.error("RSA getPrivateKey fail", e);
        }
        return null;
    }

    // ============= 3. 签名

    /**
     * 生成RSA签名
     *
     * @param signAlgorithm 签名算法
     * @param privateKeyStr 私钥
     * @param data          待签名数据
     * @param charset       字符集
     * @return
     */
    public static byte[] genSign(String signAlgorithm, String privateKeyStr, String data, String charset) {
        try {
            Signature signature = Signature.getInstance(signAlgorithm);
            PrivateKey privateKey = getPrivateKey(privateKeyStr);
            signature.initSign(privateKey);
            if (null == charset || "".equals(charset.trim())) {
                signature.update(data.getBytes());
            } else {
                signature.update(data.getBytes(charset));
            }
            return signature.sign();
        } catch (Throwable e) {
            logger.error("生成RSA签名 fail, signAlgorithm=" + signAlgorithm, e);
        }
        return null;
    }

    /**
     * 生成RSA签名
     *
     * @param signAlgorithm 签名算法
     * @param privateKeyStr 私钥
     * @param data          待签名数据
     * @param charset       字符集
     * @param urlSafe       url safe模式,字符+和/分别变成-和_
     * @return
     */
    public static String genSign(String signAlgorithm, String privateKeyStr, String data, String charset, boolean urlSafe) {
        byte[] signData = RSA.genSign(signAlgorithm, privateKeyStr, data, charset);
        if (signData == null || signData.length == 0)
            return null;

        return Base64Util.encodeBase64String(signData, urlSafe);
    }

    /**
     * 生成RSA签名（SHA1WithRSA）
     *
     * @param privateKeyStr 私钥
     * @param data          待签名数据
     * @param charset       字符集
     * @param urlSafe       url safe模式,字符+和/分别变成-和_
     * @return
     */
    public static String genSignWithSHA1(String privateKeyStr, String data, String charset, boolean urlSafe) {
        return RSA.genSign(SIGN_ALGORITHM_SHA1, privateKeyStr, data, charset, urlSafe);
    }

    public static String genSignWithSHA1(String privateKeyStr, String data) {
        return RSA.genSign(SIGN_ALGORITHM_SHA1, privateKeyStr, data, null, true);
    }

    /**
     * 生成RSA签名（SHA256WithRSA）
     *
     * @param privateKeyStr 私钥
     * @param data          待签名数据
     * @param charset       字符集
     * @param urlSafe       url safe模式,字符+和/分别变成-和_
     * @return
     */
    public static String genSignWithSHA256(String privateKeyStr, String data, String charset, boolean urlSafe) {
        return RSA.genSign(SIGN_ALGORITHM_SHA256, privateKeyStr, data, charset, urlSafe);
    }

    public static String genSignWithSHA256(String privateKeyStr, String data) {
        return RSA.genSign(SIGN_ALGORITHM_SHA256, privateKeyStr, data, null, true);
    }

    // ============= 4. 验签


    /**
     * 验证RSA签名
     *
     * @param signAlgorithm 签名算法
     * @param publicKeyStr  公钥
     * @param data          待签名数据
     * @param charset       字符集
     * @param sign          签名
     * @return
     */
    public static boolean verifySign(String signAlgorithm, String publicKeyStr, String sign, String data, String charset) {
        try {
            Signature signatureChecker = Signature.getInstance(signAlgorithm);
            PublicKey publicKey = getPublicKey(publicKeyStr);
            signatureChecker.initVerify(publicKey);

            if (null == charset || "".equals(charset.trim())) {
                signatureChecker.update(data.getBytes());
            } else {
                signatureChecker.update(data.getBytes(charset));
            }

            // 验证签名是否正常
            byte[] signBytes = Base64Util.decodeBase64(sign);

            if (signatureChecker.verify(signBytes)) {
                return true;
            }
        } catch (Throwable t) {
            logger.error("验证RSA签名 fail, signAlgorithm=" + signAlgorithm, t);
        }
        return false;
    }


    /**
     * 验证RSA签名（SHA1WithRSA）
     *
     * @param publicKeyStr 公钥
     * @param data         待签名数据
     * @param charset      字符集
     * @param sign         签名
     * @return
     */
    public static boolean verifySignBySHA1(String publicKeyStr, String sign, String data, String charset) {
        return RSA.verifySign(SIGN_ALGORITHM_SHA1, publicKeyStr, sign, data, charset);
    }

    public static boolean verifySignBySHA1(String publicKeyStr, String sign, String data) {
        return RSA.verifySign(SIGN_ALGORITHM_SHA1, publicKeyStr, sign, data, null);
    }

    /**
     * 验证RSA签名（SHA256WithRSA）
     *
     * @param publicKeyStr 公钥
     * @param data         待签名数据
     * @param charset      字符集
     * @param sign         签名
     * @return
     */
    public static boolean verifySignBySHA256(String publicKeyStr, String sign, String data, String charset) {
        return RSA.verifySign(SIGN_ALGORITHM_SHA256, publicKeyStr, sign, data, charset);
    }

    public static boolean verifySignBySHA256(String publicKeyStr, String sign, String data) {
        return RSA.verifySign(SIGN_ALGORITHM_SHA256, publicKeyStr, sign, data, null);
    }


    public static void main(String[] args) {

        Tuple2<String, String> keys = RSA.genKeyPairStr("1234", 1024);
        System.out.println("公钥");
        System.out.println(Base64Util.formatBase64Str(keys._2()));
        System.out.println("私钥");
        System.out.println(Base64Util.formatBase64Str(keys._2()));

        String data = "123wsdfQwehPE8ynEwZkHm0XRi80sMQnNJ85wnVqssuV+jAWnR565glvy2ks9aLz6TZAkA5KZ+1axquBMlmKpUaQFv7LXipfkVWF16h4QeWGA/h9xreRcAnt5rbwk2JFD0DupQeZelEWrHxxvaAWBkFhSsFAkBcwTiApqV+I6nAWKv16llzaIvKDqn7hPpXVNM7iV/VipM/zETXeJMvBSHJAm2OdD5eL6UlQ7WttaIS";
        data = "123465798";

        System.out.println();
        String signSHA1 = RSA.genSignWithSHA1(keys._2(), data);
        System.out.println("SHA1签名 = " + signSHA1);

        boolean checkSHA1 = RSA.verifySignBySHA1(keys._1(), signSHA1, data);
        System.out.println("SHA1验签 = " + checkSHA1);

        System.out.println();
        String signSHA256 = RSA.genSignWithSHA256(keys._2(), data);
        System.out.println("SHA256签名 = " + signSHA256);

        boolean checkSHA256 = RSA.verifySignBySHA256(keys._1(), signSHA256, data);
        System.out.println("SHA256验签 = " + checkSHA256);




    }
}
