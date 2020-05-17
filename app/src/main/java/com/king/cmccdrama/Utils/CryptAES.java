package com.king.cmccdrama.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.nio.charset.StandardCharsets;

/**
 * AES 加解密
 */
public class CryptAES {

    /**
     * 算法/模式/填充
     **/
    private static final String CipherMode = "AES/CBC/PKCS7Padding";

    /**
     * 加密字符串数据, 返回的字节数据还需转化成16进制字符串
     */
    public static String encrypt(String content, String key, String iv) {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        data = encrypt(data, key, iv);
        if (data != null) {
            return byte2hex(data);
        } else {
            return null;
        }
    }

    /**
     * 解密(输出结果为字符串), 密文为16进制的字符串
     */
    public static String decrypt(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = hex2byte(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decrypt(data, password, iv);
        if (data == null) return null;
        return new String(data, StandardCharsets.UTF_8);
    }

    // 创建密钥, 长度为128位(16bytes), 且转成字节格式
    private static SecretKeySpec createKey(String key) {
        if (key == null) {
            key = "";
        }
        StringBuilder sb = new StringBuilder(16);
        sb.append(key);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(data, "AES");
    }

    // 创建初始化向量, 长度为16bytes, 向量的作用其实就是salt
    private static IvParameterSpec createIV(String iv) {
        if (iv == null) {
            iv = "";
        }
        StringBuilder sb = new StringBuilder(16);
        sb.append(iv);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
        return new IvParameterSpec(data);
    }

    // 加密字节数据, 被加密的数据需要提前转化成字节格式
    private static byte[] encrypt(byte[] content, String key, String iv) {
        try {
            SecretKeySpec secretKeySpec = createKey(key);
            IvParameterSpec ivParameterSpec = createIV(iv);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(content); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密字节数组
    private static byte[] decrypt(byte[] content, String key, String iv) {
        try {
            SecretKeySpec secretKeySpec = createKey(key);
            IvParameterSpec ivParameterSpec = createIV(iv);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 字节数组转成16进制大写字符串
    private static String byte2hex(byte[] b) {
        String tmp;
        StringBuilder sb = new StringBuilder(b.length * 2);
        /* 0403
        for (int n = 0; n < b.length; n++) {
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        */
        for (byte aB : b) {
            tmp = (Integer.toHexString(aB & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    // 将16进制字符串转换成字节数组
    private static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = inputString.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }
}
