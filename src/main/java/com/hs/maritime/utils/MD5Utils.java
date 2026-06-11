package com.hs.maritime.utils;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 */
public class MD5Utils {

    // 将字节数组转换为对应的十六进制字符串。
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    // 将单个字节转换为对应的两个十六进制字符
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    // 接收一个字符串和字符集名称作为参数，对输入的字符串进行MD5加密，并返回加密后的结果。
    public static String MD5Encode(String origin, String charset) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charset == null || "".equals(charset)) {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charset)));
            }
        } catch (Exception exception) {

        }
        return resultString;
    }

    // 这个数组用于将字节转换为十六进制字符。
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    public static void main(String[] args) {
        System.out.println(MD5Encode("123456", "UTF-8"));
    }
}