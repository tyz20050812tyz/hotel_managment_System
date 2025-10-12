package com.hotel.util;

import java.util.regex.Pattern;

/**
 * 工具类，提供常用的验证和处理方法
 */
public class Utils {
    
    // 身份证号正则表达式（18位）
    private static final Pattern ID_CARD_PATTERN = 
        Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    
    // 手机号正则表达式（11位，1开头）
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^1[3-9]\\d{9}$");
    
    // 邮箱正则表达式
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    /**
     * 验证身份证号格式是否正确
     * @param idCard 身份证号
     * @return true-格式正确，false-格式错误
     */
    public static boolean validateIdCard(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return false;
        }
        
        String trimmedIdCard = idCard.trim();
        
        // 基本格式验证
        if (!ID_CARD_PATTERN.matcher(trimmedIdCard).matches()) {
            return false;
        }
        
        // 校验码验证
        return validateIdCardChecksum(trimmedIdCard);
    }
    
    /**
     * 验证身份证号校验码
     * @param idCard 18位身份证号
     * @return true-校验码正确，false-校验码错误
     */
    private static boolean validateIdCardChecksum(String idCard) {
        if (idCard.length() != 18) {
            return false;
        }
        
        // 加权因子
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 校验码对照表
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = idCard.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            sum += (c - '0') * weights[i];
        }
        
        int remainder = sum % 11;
        char expectedCheckCode = checkCodes[remainder];
        char actualCheckCode = Character.toUpperCase(idCard.charAt(17));
        
        return expectedCheckCode == actualCheckCode;
    }
    
    /**
     * 验证手机号格式是否正确
     * @param phone 手机号
     * @return true-格式正确，false-格式错误
     */
    public static boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * 验证邮箱格式是否正确
     * @param email 邮箱地址
     * @return true-格式正确，false-格式错误
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * 判断字符串是否为空或null
     * @param str 字符串
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 判断字符串是否不为空
     * @param str 字符串
     * @return true-不为空，false-为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 安全地去除字符串两端空格
     * @param str 字符串
     * @return 去除空格后的字符串，如果输入为null则返回null
     */
    public static String safeTrim(String str) {
        return str == null ? null : str.trim();
    }
    
    /**
     * 格式化手机号（中间4位用*代替）
     * @param phone 手机号
     * @return 格式化后的手机号
     */
    public static String formatPhone(String phone) {
        if (!validatePhone(phone)) {
            return phone;
        }
        
        String trimmedPhone = phone.trim();
        if (trimmedPhone.length() == 11) {
            return trimmedPhone.substring(0, 3) + "****" + trimmedPhone.substring(7);
        }
        
        return phone;
    }
    
    /**
     * 格式化身份证号（中间部分用*代替）
     * @param idCard 身份证号
     * @return 格式化后的身份证号
     */
    public static String formatIdCard(String idCard) {
        if (!validateIdCard(idCard)) {
            return idCard;
        }
        
        String trimmedIdCard = idCard.trim();
        if (trimmedIdCard.length() == 18) {
            return trimmedIdCard.substring(0, 6) + "********" + trimmedIdCard.substring(14);
        }
        
        return idCard;
    }
}