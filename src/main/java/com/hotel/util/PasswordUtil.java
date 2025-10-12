package com.hotel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 密码加密工具类
 */
public class PasswordUtil {
    
    private static final String ALGORITHM = "MD5";
    private static final String CHARSET = "UTF-8";
    
    /**
     * 对密码进行MD5加密
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encrypt(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] bytes = md.digest(password.getBytes(CHARSET));
            return bytesToHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Password encryption failed", e);
        }
    }
    
    /**
     * 验证密码
     * @param password 原始密码
     * @param encryptedPassword 加密后的密码
     * @return 密码正确返回true
     */
    public static boolean verify(String password, String encryptedPassword) {
        if (password == null || encryptedPassword == null) {
            return false;
        }
        
        String encrypted = encrypt(password);
        return encrypted.equals(encryptedPassword);
    }
    
    /**
     * 生成随机密码
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4");
        }
        
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * 验证密码强度
     * @param password 密码
     * @return 强度等级（1-5，5为最强）
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }
        
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        // 纯小写字母密码特殊处理
        if (hasLower && !hasUpper && !hasDigit && !hasSpecial) {
            // 短密码（小于8字符）强度为1
            if (password.length() < 8) {
                return 1;
            }
            // 中等长度密码（8-15字符）强度为1  
            if (password.length() < 16) {
                return 1;
            }
            // 很长的纯小写密码（16字符及以上）强度为2
            return 2;
        }
        
        // 混合字符类型的密码评分
        int score = 1; // 基础分数
        
        // 长度奖励
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        
        // 字符类型奖励
        if (hasUpper) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;
        
        return Math.min(score, 5);
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}