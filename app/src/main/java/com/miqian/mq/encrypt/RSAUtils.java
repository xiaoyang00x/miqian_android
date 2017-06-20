package com.miqian.mq.encrypt;

import android.text.TextUtils;
import android.util.Base64;

import com.miqian.mq.net.Urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by Jackie on 2015/9/9.
 */
public class RSAUtils {


    private static final String RSA = "RSA";
    protected static final boolean isEncrypt = false;

    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String algorithm, String bysKey) throws Exception {
        byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param content
     * @return
     */
    public static String encryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(RSA, Urls.getPubliceKey());

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte[] output = cipher.doFinal(content.getBytes("UTF-8"));

            return new String(Base64.encode(output, Base64.DEFAULT));
        } catch (Exception e) {
            return null;
        }
    }

    public static String encryptURLEncode(String content) {
        if (TextUtils.isEmpty(content)){
            return "";
        }
        if (!isEncrypt) {
            return content;
        }
        try {
            return URLEncoder.encode(encryptByPublic(content), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用私钥解密
     *
     * @param content 密文
     * @return 解密后的字符串
     */
    public static String decryptByPrivate(String content) {
        if (!isEncrypt) {
            return content;
        }
        try {
            PrivateKey privateKey = loadPrivateKey(Urls.getPrivateKey());
            byte[] encryptedData = Base64.decode(content, Base64.DEFAULT);
            byte[] output = decryptData(encryptedData, privateKey);
            return new String(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 用私钥解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param privateKey    私钥
     * @return
     */
    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 从字符串中加载私钥
     * <p/>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr, Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA, "BC");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }
}
